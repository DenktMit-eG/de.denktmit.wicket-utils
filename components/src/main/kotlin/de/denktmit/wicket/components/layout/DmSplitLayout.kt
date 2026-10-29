package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.request.resource.ResourceReference

/**
 * Two panes with a divider (requirement C.16), side by side or stacked. The first pane starts at [firstSize]; the
 * browser's own `resize` handle on it resizes without script, and the divider drags with script. With
 * [collapsible] each pane gets a collapse control that script serves. The panes are the components [first] and
 * [second], whose ids must be [FIRST] and [SECOND].
 */
open class DmSplitLayout
@JvmOverloads
constructor(
  id: String,
  var orientation: Orientation = Orientation.HORIZONTAL,
  first: Component,
  second: Component,
  /** The size of the first pane along the split, a CSS length. */
  var firstSize: String = "50%",
  var collapsible: Boolean = true,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private val first = first.also { require(it.id == FIRST) { "first pane id must be '$FIRST'" } }
  private val second = second.also { require(it.id == SECOND) { "second pane id must be '$SECOND'" } }

  override fun addComponents() {
    outputMarkupId = true
    +WebMarkupContainer("firstPane").apply { add(first) }
    +WebMarkupContainer("secondPane").apply { add(second) }
    +object : WebMarkupContainer("divider") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("aria-orientation", if (orientation == Orientation.HORIZONTAL) "vertical" else "horizontal")
      }
    }.apply {
      add(WebMarkupContainer("collapseFirst").apply { isVisible = collapsible; add(DmIcon("icon", "chevron-left")) })
      add(WebMarkupContainer("collapseSecond").apply { isVisible = collapsible; add(DmIcon("icon", "chevron-right")) })
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-split", orientation.name.lowercase())
    tag.append("class", "dm-split-${orientation.name.lowercase()}", " ")
    tag.append("style", "--first-size:$firstSize", ";")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-split-layout")

  override fun script(): ResourceReference = DmResources.js("dm-split-layout")

  companion object {
    /** The id of the first pane's component. */
    const val FIRST = "first"

    /** The id of the second pane's component. */
    const val SECOND = "second"
  }
}
