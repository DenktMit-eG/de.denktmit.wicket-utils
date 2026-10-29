package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.request.resource.ResourceReference

/**
 * A menu bar (requirement D.31): menus with items, sub-menus, separators, check and radio items, icons, shortcuts
 * shown as `kbd` and bound by script, disabled items. Items are links, so every action works without script;
 * script adds the arrow-key navigation of a menubar and the shortcuts; sub-menus open on hover and focus by CSS.
 */
open class DmMenuBar
@JvmOverloads
constructor(
  id: String,
  val items: List<MenuItem>,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  override fun addComponents() {
    outputMarkupId = true
    +MenuPanel("menu", items, items, this, true)
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-menu", "bar")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-menu")

  override fun script(): ResourceReference = DmResources.js("dm-menu")
}
