package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebComponent

/**
 * A visible gap (requirement C.25): renders `<hr>` with the orientation announced, [size] as the margin around it.
 * A vertical separator stands between the items of a row.
 */
open class DmSeparator
@JvmOverloads
constructor(
  id: String,
  var orientation: Orientation = Orientation.HORIZONTAL,
  var size: Space = Space.S0,
) : WebComponent(id) {

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.name = "hr"
    tag.put("role", "separator")
    tag.put("aria-orientation", orientation.name.lowercase())
    tag.append("class", if (orientation == Orientation.VERTICAL) "dm-separator-vertical" else "dm-separator-horizontal", " ")
    tag.append("style", "--size:${size.css}", ";")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-separator")))
  }
}

/** An invisible gap of [size] (requirement C.25), hidden from assistive technology. */
open class DmSpace
@JvmOverloads
constructor(
  id: String,
  var orientation: Orientation = Orientation.HORIZONTAL,
  var size: Space = Space.S0,
) : WebComponent(id) {

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("aria-hidden", "true")
    tag.append("class", if (orientation == Orientation.VERTICAL) "dm-space-vertical" else "dm-space-horizontal", " ")
    tag.append("style", "--size:${size.css}", ";")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-separator")))
  }
}
