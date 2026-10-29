package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/** The five regions of a [DmBorderLayout]; the enum name in lower case is the id a region's component must use. */
enum class Region {
  TOP, BOTTOM, START, END, CENTRE;

  /** The component id of this region. */
  val id: String get() = name.lowercase()
}

/**
 * Five regions, each optional (requirement C.13): top and bottom span the width, start and end flank the centre,
 * the centre fills what the others leave. A region placed with [region] may be collapsible (it becomes a `details`
 * with the title as its `summary`, no script needed) and resizable (the browser's own `resize` handle). The
 * component of a region carries the region's id ([Region.id]).
 */
open class DmBorderLayout
@JvmOverloads
constructor(
  id: String,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private class Slot(val component: Component, val title: IModel<String>, val collapsible: Boolean, val resizable: Boolean, val open: Boolean)

  private val slots = mutableMapOf<Region, Slot>()

  /** Places [component] (id [Region.id]) in [region]. */
  @JvmOverloads
  fun region(
    region: Region,
    component: Component,
    title: IModel<String> = Model.of(region.name.lowercase().replaceFirstChar { it.uppercase() }),
    collapsible: Boolean = false,
    resizable: Boolean = false,
    open: Boolean = true,
  ): DmBorderLayout {
    require(component.id == region.id) { "component id for $region must be '${region.id}', was '${component.id}'" }
    slots[region] = Slot(component, title, collapsible, resizable, open)
    return this
  }

  override fun addComponents() {
    outputMarkupId = true
    Region.entries.forEach { region ->
      val slot = slots[region]
      val box = object : WebMarkupContainer(region.id + "Box") {
        override fun onComponentTag(tag: ComponentTag) {
          super.onComponentTag(tag)
          if (slot?.collapsible == true) {
            tag.name = "details"
            if (slot.open) tag.put("open", "open")
          }
          if (slot?.resizable == true) tag.append("class", "dm-region-resizable", " ")
        }
      }
      box.isVisible = slot != null
      box.add(Label(region.id + "Title", slot?.title ?: Model.of("")).apply { isVisible = slot?.collapsible == true })
      box.add(slot?.component ?: WebMarkupContainer(region.id))
      +box
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-border-layout")
}
