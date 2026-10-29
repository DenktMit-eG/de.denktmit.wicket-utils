package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Page
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One step of a [DmBreadcrumb]: a label and the page it leads to. */
data class Crumb
@JvmOverloads
constructor(val label: String, val page: Class<out Page>? = null, val parameters: PageParameters = PageParameters()) : Serializable

/**
 * The path to the current page (requirement D.34): links on every step but the last, which is the current page
 * and announced as such. Pages are bookmarkable, so the breadcrumb is a list of page links, not a panel history.
 */
open class DmBreadcrumb
@JvmOverloads
constructor(
  id: String,
  crumbs: IModel<List<Crumb>>,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, crumbs, init) {

  @JvmOverloads
  constructor(id: String, crumbs: List<Crumb>, init: DmWidgetPanel.() -> Unit = {}) : this(id, Model.ofList(crumbs), init)

  override fun addComponents() {
    @Suppress("UNCHECKED_CAST")
    +object : ListView<Crumb>("crumbs", defaultModel as IModel<List<Crumb>>) {
      override fun populateItem(item: ListItem<Crumb>) {
        val crumb = item.modelObject
        val last = item.index == list.size - 1
        val linked = !last && crumb.page != null
        item.add(pageLink("link", crumb.page ?: Page::class.java, crumb.parameters).apply { isVisible = linked; add(Label("linkLabel", crumb.label)) })
        item.add(Label("label", crumb.label).apply {
          isVisible = !linked
          if (last) add(org.apache.wicket.AttributeModifier.replace("aria-current", "page"))
        })
      }
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("aria-label", "Breadcrumb")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-breadcrumb")
}
