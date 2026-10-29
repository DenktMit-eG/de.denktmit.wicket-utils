package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A popup menu (requirement D.32) with the same content model as the menu bar: a `details` element whose
 * `summary` is the trigger, so it opens and closes without script. [openFrom] binds it to another widget by
 * script: a click or a right click on that widget opens the menu where the pointer is.
 */
open class DmPopupMenu
@JvmOverloads
constructor(
  id: String,
  val items: List<MenuItem>,
  /** The text of the built-in trigger. */
  val label: IModel<String> = Model.of("Menu"),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private var openFrom: Component? = null

  /** Opens the menu on a click or a right click on [component] as well. */
  fun openFrom(component: Component): DmPopupMenu {
    component.outputMarkupId = true
    openFrom = component
    return this
  }

  override fun addComponents() {
    outputMarkupId = true
    +org.apache.wicket.markup.html.basic.Label("label", label)
    +DmIcon("icon", "more")
    +MenuPanel("menu", items, items, this, false)
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-menu", "popup")
    openFrom?.let { tag.put("data-dm-menu-for", it.markupId) }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-menu")

  override fun script(): ResourceReference = DmResources.js("dm-menu")
}
