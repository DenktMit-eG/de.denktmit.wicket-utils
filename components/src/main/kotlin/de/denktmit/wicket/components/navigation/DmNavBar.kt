package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.layout.Orientation
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One entry of a [DmNavBar]: a label, the page it leads to, an icon, a badge and sub-items. */
data class NavItem
@JvmOverloads
constructor(
  val label: String,
  val page: Class<out Page>,
  val parameters: PageParameters = PageParameters(),
  val icon: String? = null,
  val badge: String? = null,
  val children: List<NavItem> = emptyList(),
) : Serializable

/**
 * A navigation bar (requirement D.33), vertical or horizontal, with items and sub-items, icons, badges, a
 * collapsed mode that shows icons only (labels stay for assistive technology), and the current item marked with
 * `aria-current` when its page is the page shown.
 */
open class DmNavBar
@JvmOverloads
constructor(
  id: String,
  val items: List<NavItem>,
  var orientation: Orientation = Orientation.VERTICAL,
  var collapsed: Boolean = false,
  val label: IModel<String> = Model.of("Main"),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private fun link(id: String, item: NavItem): Component = pageLink(id, item.page, item.parameters).apply {
    add(DmIcon("icon", item.icon ?: "").apply { isVisible = item.icon != null })
    add(Label("label", item.label))
    add(Label("badge", item.badge ?: "").apply { isVisible = item.badge != null })
    add(object : org.apache.wicket.behavior.Behavior() {
      override fun onComponentTag(component: Component, tag: ComponentTag) {
        if (item.page == page.javaClass) tag.put("aria-current", "page")
        if (collapsed) tag.put("title", item.label)
      }
    })
  }

  override fun addComponents() {
    +object : ListView<NavItem>("items", items) {
      override fun populateItem(entry: ListItem<NavItem>) {
        val item = entry.modelObject
        entry.add(link("link", item))
        entry.add(WebMarkupContainer("children").apply {
          isVisible = item.children.isNotEmpty()
          add(object : ListView<NavItem>("sub", item.children) {
            override fun populateItem(child: ListItem<NavItem>) {
              child.add(link("link", child.modelObject))
            }
          })
        })
      }
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("aria-label", label.`object`)
    tag.append("class", "dm-nav-bar-${orientation.name.lowercase()}", " ")
    if (collapsed) tag.append("class", "dm-nav-bar-collapsed", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-nav-bar")
}
