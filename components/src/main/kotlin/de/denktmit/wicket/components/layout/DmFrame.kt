package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/**
 * An external document embedded at a size (requirement C.27): an `<iframe>` with the address from [src], the title
 * assistive technology announces, a width and height, lazy loading and a sandbox that an author widens on purpose.
 */
open class DmFrame
@JvmOverloads
constructor(
  id: String,
  src: IModel<String>,
  /** What the frame shows, announced as its accessible name. */
  val title: IModel<String>,
  /** The width, a CSS length. */
  var width: String = "100%",
  /** The height, a CSS length. */
  var height: String = "30rem",
  /** The `sandbox` attribute; `null` applies none, `""` the strictest. */
  var sandbox: String? = "allow-scripts allow-same-origin",
) : WebMarkupContainer(id, src) {

  @JvmOverloads
  constructor(id: String, src: String, title: String, width: String = "100%", height: String = "30rem") :
    this(id, Model.of(src), Model.of(title), width, height)

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    checkComponentTag(tag, "iframe")
    tag.put("src", defaultModelObjectAsString)
    tag.put("title", title.`object`)
    tag.put("loading", "lazy")
    tag.append("style", "width:$width;height:$height", ";")
    sandbox?.let { tag.put("sandbox", it) }
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-frame")))
  }
}
