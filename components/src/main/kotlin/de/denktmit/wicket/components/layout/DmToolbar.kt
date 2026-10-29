package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebComponent
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.request.resource.ResourceReference

/**
 * A row of actions (requirement C.28): items join through [action], groups are divided by [separator], [spacer]
 * pushes what follows to the far side, [justify] aligns the row. When the row runs out of space, script moves the
 * items that no longer fit into a "more" menu at the end; without script the row wraps.
 */
open class DmToolbar
@JvmOverloads
constructor(
  id: String,
  var justify: Justify = Justify.START,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private val items = RepeatingView("items")

  /** Adds an action; [factory] receives the id the component must use. */
  fun action(factory: (String) -> Component): DmToolbar {
    items.add(factory(items.newChildId()))
    return this
  }

  /** Adds a separator between groups. */
  fun separator(): DmToolbar {
    items.add(object : WebComponent(items.newChildId()) {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("role", "separator")
        tag.put("aria-orientation", "vertical")
        tag.put("class", "dm-toolbar-separator")
      }
    })
    return this
  }

  /** Adds a gap that takes the remaining space. */
  fun spacer(): DmToolbar {
    items.add(object : WebComponent(items.newChildId()) {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("aria-hidden", "true")
        tag.put("class", "dm-toolbar-spacer")
      }
    })
    return this
  }

  override fun addComponents() {
    add(items)
    add(DmIcon("moreIcon", "more"))
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-toolbar")

  override fun script(): ResourceReference = DmResources.js("dm-toolbar")

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", "toolbar")
    tag.put("data-dm-toolbar", "")
    tag.append("style", "--justify:${justify.css}", ";")
  }
}
