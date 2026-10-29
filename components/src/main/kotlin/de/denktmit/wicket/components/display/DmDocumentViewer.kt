package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A document shown with the browser's own viewer (requirement J.100), PDF first: an `object` element over the
 * document's address in a region of the page, a link that opens it in a new tab and a download link. The toolbox
 * builds no viewer of its own; where the browser has none, the fallback text and the links remain.
 */
open class DmDocumentViewer
@JvmOverloads
constructor(
  id: String,
  val src: IModel<String>,
  val title: IModel<String>,
  /** The media type the browser picks its viewer by. */
  val type: String = "application/pdf",
  /** The height of the region, a CSS length. */
  var height: String = "40rem",
  /** The file name the download gets, `null` for the browser's choice. */
  val fileName: String? = null,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, src, init) {

  @JvmOverloads
  constructor(id: String, src: String, title: String, type: String = "application/pdf", height: String = "40rem", fileName: String? = null) :
    this(id, Model.of(src), Model.of(title), type, height, fileName)

  override fun addComponents() {
    +object : WebMarkupContainer("document") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("data", src.`object`)
        tag.put("type", type)
        tag.put("aria-label", title.`object`)
        tag.append("style", "height:$height", ";")
      }
    }.apply { add(Label("fallback", title)) }
    +object : WebMarkupContainer("open") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("href", src.`object`)
      }
    }
    +object : WebMarkupContainer("download") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("href", src.`object`)
        tag.put("download", fileName ?: "")
      }
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-display")
}
