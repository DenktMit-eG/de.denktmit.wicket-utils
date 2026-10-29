package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.tree.DefaultTableTree
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse

/**
 * A tree with columns (requirement F.62): Wicket's `DefaultTableTree` over the same provider as [DmTree], the
 * first column a `TreeColumn` with the expand control, the others any columns, paging by [rowsPerPage]. The
 * toolbox adds the hooks and the sheet.
 */
open class DmTreeTable<T, S>
@JvmOverloads
constructor(
  id: String,
  columns: List<IColumn<T, S>>,
  provider: ISortableTreeProvider<T, S>,
  rowsPerPage: Int = 25,
) : DefaultTableTree<T, S>(id, columns, provider, rowsPerPage) {

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    outputMarkupId = true
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", "treegrid")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-tree")))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-table")))
  }
}
