package de.denktmit.wicket.components.collection

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.navigation.DmPager
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.markup.repeater.data.DataView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable
import java.util.Optional

/**
 * A table over a data source (requirement F.59): columns with header, an auxiliary header row of column groups
 * and a total row; sorting by column; paging through [DmPager]; column hiding through a column menu, frozen
 * columns by CSS, resizing and reordering by script; row selection of one or many through a [DmSelectionModel]
 * (a check column or a click on the row); a detail region per row; group headers when [groupBy] changes between
 * rows; in-place editing of cells whose column names an editor; a row class by rule; an empty message.
 *
 * The toolbox builds the table over Wicket's `DataView` and `ISortableDataProvider` with its own markup, because
 * `DataTable`'s fixed row markup leaves no room for detail and group rows: every row is a `tbody` that holds the
 * group header, the row and the detail.
 */
open class DmTable<T : Serializable, S>
@JvmOverloads
constructor(
  id: String,
  val columns: List<DmColumn<T, S>>,
  val source: ISortableDataProvider<T, S>,
  val rowsPerPage: Int = 25,
  /** The selection; `null` for none. */
  val selection: DmSelectionModel<T>? = null,
  /** Switch sorting and paging by Ajax, with the links as fallback. */
  val ajax: Boolean = false,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  /** The detail region of a row; the component gets the id given and the row model. `null` for no details. */
  @Transient
  var detail: ((String, IModel<T>) -> Component)? = null

  /** The group a row belongs to; a header row appears where it changes. `null` for no grouping. */
  @Transient
  var groupBy: ((T) -> String)? = null

  /** A CSS class per row, or `null`. */
  @Transient
  var rowClass: ((T) -> String?)? = null

  /** Column groups above the headers: title and the number of columns each spans. */
  var headerGroups: List<Pair<String, Int>> = emptyList()

  /** Show a check column for the selection. */
  var selectColumn: Boolean = true

  /** Let the user hide and show columns. */
  var columnMenu: Boolean = false

  /** The text shown when there are no rows. */
  var emptyMessage: IModel<String> = Model.of("Nothing to show")

  /** The rows whose detail is open. */
  val expanded: MutableSet<T> = LinkedHashSet()

  private lateinit var rows: DataView<T>
  private var lastGroup: String? = null
  private val pageRows = ArrayList<T>()

  /** The columns shown, in order. */
  fun visibleColumns(): List<DmColumn<T, S>> = columns.filterNot { it.hidden }

  /** The rows of the current page as rendered last. */
  fun currentRows(): List<T> = pageRows

  /** The data view, a pageable for the pager and for the page's own navigation. */
  val dataView: DataView<T> get() = rows

  private fun refresh(target: Optional<AjaxRequestTarget>) {
    target.ifPresent { it.add(this) }
  }

  override fun addComponents() {
    outputMarkupId = true
    rows = object : DataView<T>("rows", source, rowsPerPage.toLong()) {
      override fun onBeforeRender() {
        lastGroup = null
        pageRows.clear()
        super.onBeforeRender()
      }

      override fun populateItem(item: Item<T>) {
        val row = item.modelObject
        pageRows.add(row)
        val group = groupBy?.invoke(row)
        val newGroup = group != null && group != lastGroup
        lastGroup = group
        item.add(WebMarkupContainer("groupRow").apply {
          isVisible = newGroup
          add(Label("groupTitle", group ?: "").apply { add(org.apache.wicket.AttributeModifier.replace("colspan", "${visibleColumns().size + 2}")) })
        })
        item.add(object : WebMarkupContainer("row") {
          override fun onComponentTag(tag: ComponentTag) {
            super.onComponentTag(tag)
            rowClass?.invoke(row)?.let { tag.append("class", it, " ") }
            if (selection?.isSelected(row) == true) tag.put("aria-selected", "true")
            if (selection != null) tag.put("data-dm-table-row", "")
          }
        }.apply {
          add(object : AjaxFallbackLink<Void>("select") {
            override fun onClick(target: Optional<AjaxRequestTarget>) {
              selection?.toggle(row, target.orElse(null))
              refresh(target)
            }

            override fun onConfigure() {
              super.onConfigure()
              isVisible = selection != null && selectColumn
            }

            override fun onComponentTag(tag: ComponentTag) {
              super.onComponentTag(tag)
              tag.put("role", "checkbox")
              tag.put("aria-checked", (selection?.isSelected(row) == true).toString())
              tag.put("data-dm-table-select", "")
            }
          }.apply { add(DmIcon("icon", "check")) })
          add(object : AjaxFallbackLink<Void>("toggle") {
            override fun onClick(target: Optional<AjaxRequestTarget>) {
              if (!expanded.remove(row)) expanded.add(row)
              refresh(target)
            }

            override fun onConfigure() {
              super.onConfigure()
              isVisible = detail != null
            }

            override fun onComponentTag(tag: ComponentTag) {
              super.onComponentTag(tag)
              tag.put("aria-expanded", (row in expanded).toString())
              tag.put("aria-label", if (row in expanded) "Hide details" else "Show details")
            }
          }.apply { add(DmIcon("icon", "chevron-right")) })
          add(object : ListView<DmColumn<T, S>>("cells", visibleColumns()) {
            override fun populateItem(cell: ListItem<DmColumn<T, S>>) {
              val column = cell.modelObject
              cell.add(cellComponent(column, item.model))
              cell.add(object : org.apache.wicket.behavior.Behavior() {
                override fun onComponentTag(component: Component, tag: ComponentTag) {
                  column.cssClass?.let { tag.append("class", it, " ") }
                  if (column.frozen) tag.append("class", "dm-table-frozen", " ")
                }
              })
            }
          })
        })
        item.add(WebMarkupContainer("detailRow").apply {
          val open = detail != null && row in expanded
          isVisible = open
          add(if (open) detail!!("detail", item.model) else WebMarkupContainer("detail"))
          get("detail").add(org.apache.wicket.AttributeModifier.replace("colspan", "${visibleColumns().size + 2}"))
        })
      }
    }
    +rows
    +object : ListView<Pair<String, Int>>("headerGroups", object : IModel<List<Pair<String, Int>>> { override fun getObject() = headerGroups }) {
      override fun populateItem(item: ListItem<Pair<String, Int>>) {
        item.add(Label("group", item.modelObject.first).apply { add(org.apache.wicket.AttributeModifier.replace("colspan", item.modelObject.second.toString())) })
      }

      override fun onConfigure() {
        super.onConfigure()
        isVisible = headerGroups.isNotEmpty()
      }
    }
    +WebMarkupContainer("selectHead").apply { add(object : org.apache.wicket.behavior.Behavior() {
      override fun onConfigure(component: Component) {
        component.isVisible = selection != null && selectColumn
      }
    }) }
    +WebMarkupContainer("detailHead").apply { add(object : org.apache.wicket.behavior.Behavior() {
      override fun onConfigure(component: Component) {
        component.isVisible = detail != null
      }
    }) }
    +object : ListView<DmColumn<T, S>>("headers", object : IModel<List<DmColumn<T, S>>> { override fun getObject() = visibleColumns() }) {
      override fun populateItem(item: ListItem<DmColumn<T, S>>) {
        val column = item.modelObject
        val sortable = column.sort != null
        item.add(object : AjaxFallbackLink<Void>("sort") {
          override fun onClick(target: Optional<AjaxRequestTarget>) {
            val current = source.sortState.getPropertySortOrder(column.sort)
            source.sortState.setPropertySortOrder(column.sort, if (current == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING)
            rows.currentPage = 0
            refresh(target)
          }
        }.apply { isVisible = sortable; add(Label("title", column.title)) })
        item.add(Label("plainTitle", column.title).apply { isVisible = !sortable })
        item.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: Component, tag: ComponentTag) {
            if (sortable) {
              tag.put("aria-sort", when (source.sortState.getPropertySortOrder(column.sort)) { SortOrder.ASCENDING -> "ascending"; SortOrder.DESCENDING -> "descending"; else -> "none" })
            }
            column.cssClass?.let { tag.append("class", it, " ") }
            if (column.frozen) tag.append("class", "dm-table-frozen", " ")
          }
        })
      }
    }
    +object : WebMarkupContainer("totals") {
      override fun onConfigure() {
        super.onConfigure()
        isVisible = columns.any { it.total != null }
      }
    }.apply {
      add(object : ListView<DmColumn<T, S>>("totalCells", object : IModel<List<DmColumn<T, S>>> { override fun getObject() = visibleColumns() }) {
        override fun populateItem(item: ListItem<DmColumn<T, S>>) {
          item.add(Label("total", object : IModel<String> { override fun getObject(): String = item.modelObject.total?.invoke(pageRows) ?: "" }))
        }
      })
      add(WebMarkupContainer("totalsSelect").apply { add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: Component) {
          component.isVisible = selection != null && selectColumn
        }
      }) })
      add(WebMarkupContainer("totalsDetail").apply { add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: Component) {
          component.isVisible = detail != null
        }
      }) })
    }
    +Label("empty", emptyMessage).apply { add(object : org.apache.wicket.behavior.Behavior() {
      override fun onConfigure(component: Component) {
        component.isVisible = source.size() == 0L
      }
    }) }
    +DmPager("pager", rows, ajax).refresh(this)
    +object : WebMarkupContainer("columnMenu") {
      override fun onConfigure() {
        super.onConfigure()
        isVisible = columnMenu
      }
    }.apply {
      add(Form<Unit>("columnsForm").apply {
        add(object : ListView<DmColumn<T, S>>("columnToggles", columns) {
          override fun populateItem(item: ListItem<DmColumn<T, S>>) {
            val column = item.modelObject
            item.add(CheckBox("show", object : IModel<Boolean> {
              override fun getObject(): Boolean = !column.hidden

              override fun setObject(value: Boolean) {
                column.hidden = !value
              }
            }))
            item.add(Label("name", column.title))
          }
        })
      })
    }
  }

  private fun cellComponent(column: DmColumn<T, S>, model: IModel<T>): Component {
    column.cell?.let { return it("cell", model) }
    column.edit?.let { edit ->
      return AjaxEditableLabel("cell", object : IModel<String> {
        override fun getObject(): String = column.text(model.`object`)

        override fun setObject(value: String?) = edit(model.`object`, value ?: "")
      })
    }
    return Label("cell", object : IModel<String> { override fun getObject(): String = column.text(model.`object`) })
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-table", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-table")

  override fun script(): ResourceReference = DmResources.js("dm-table")
}
