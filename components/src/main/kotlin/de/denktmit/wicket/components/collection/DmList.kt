package de.denktmit.wicket.components.collection

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.navigation.DmPager
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
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
 * A list over a data source (requirement F.60) for rows that are not tabular: an item [template] the author
 * writes once, selection of one or many with an optional check mark, paging, sorting by the [sorts] offered,
 * group headers when [groupBy] changes, an empty message. The same source and selection model as the table.
 */
open class DmList<T : Serializable, S>
@JvmOverloads
constructor(
  id: String,
  val source: ISortableDataProvider<T, S>,
  @Transient
  private val template: (String, IModel<T>) -> Component,
  val rowsPerPage: Int = 25,
  val selection: DmSelectionModel<T>? = null,
  val ajax: Boolean = false,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  /** The sort properties offered as links: label and property. */
  var sorts: List<Pair<String, S>> = emptyList()

  /** Show a check mark on selected items. */
  var checkMarks: Boolean = true

  /** The group an item belongs to; a header appears where it changes. */
  @Transient
  var groupBy: ((T) -> String)? = null

  var emptyMessage: IModel<String> = Model.of("Nothing to show")

  private lateinit var items: DataView<T>
  private var lastGroup: String? = null

  /** The data view, a pageable. */
  val dataView: DataView<T> get() = items

  override fun addComponents() {
    outputMarkupId = true
    items = object : DataView<T>("items", source, rowsPerPage.toLong()) {
      override fun onBeforeRender() {
        lastGroup = null
        super.onBeforeRender()
      }

      override fun populateItem(item: Item<T>) {
        val row = item.modelObject
        val group = groupBy?.invoke(row)
        val newGroup = group != null && group != lastGroup
        lastGroup = group
        item.add(Label("group", group ?: "").apply { isVisible = newGroup })
        item.add(object : AjaxFallbackLink<Void>("select") {
          override fun onClick(target: Optional<AjaxRequestTarget>) {
            selection?.toggle(row, target.orElse(null))
            target.ifPresent { it.add(this@DmList) }
          }

          override fun onConfigure() {
            super.onConfigure()
            isVisible = selection != null
          }

          override fun onComponentTag(tag: ComponentTag) {
            super.onComponentTag(tag)
            tag.put("role", "checkbox")
            tag.put("aria-checked", (selection?.isSelected(row) == true).toString())
          }
        }.apply { add(DmIcon("icon", "check").apply { isVisible = checkMarks }) })
        item.add(template("item", item.model))
        item.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: Component, tag: ComponentTag) {
            if (selection?.isSelected(row) == true) tag.put("aria-selected", "true")
          }
        })
      }
    }
    +items
    +object : ListView<Pair<String, S>>("sorts", object : IModel<List<Pair<String, S>>> { override fun getObject() = sorts }) {
      override fun populateItem(item: ListItem<Pair<String, S>>) {
        val (label, property) = item.modelObject
        item.add(object : AjaxFallbackLink<Void>("sort") {
          override fun onClick(target: Optional<AjaxRequestTarget>) {
            val current = source.sortState.getPropertySortOrder(property)
            source.sortState.setPropertySortOrder(property, if (current == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING)
            items.currentPage = 0
            target.ifPresent { it.add(this@DmList) }
          }

          override fun onComponentTag(tag: ComponentTag) {
            super.onComponentTag(tag)
            val order = source.sortState.getPropertySortOrder(property)
            if (order != SortOrder.NONE) tag.put("aria-sort", if (order == SortOrder.ASCENDING) "ascending" else "descending")
          }
        }.apply { add(Label("sortLabel", label)) })
      }

      override fun onConfigure() {
        super.onConfigure()
        isVisible = sorts.isNotEmpty()
      }
    }
    +Label("empty", emptyMessage).apply { add(object : org.apache.wicket.behavior.Behavior() {
      override fun onConfigure(component: Component) {
        component.isVisible = source.size() == 0L
      }
    }) }
    +DmPager("pager", items, ajax).refresh(this)
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-list")
}
