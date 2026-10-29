package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A drawn signature captured as an image (requirement E.52): a canvas the user draws on with pointer or touch,
 * script writing the PNG as a data URL into a hidden field the form submits; the model holds that data URL or
 * `null`. Without script the canvas is inert and the value stays what it was: a signature cannot be drawn without
 * script, which is the one place the catalogue accepts that.
 */
open class DmSignature
@JvmOverloads
constructor(
  id: String,
  model: IModel<String?>,
  val width: Int = 400,
  val height: Int = 150,
  init: DmInputPanel<String?>.() -> Unit = {},
) : DmInputPanel<String?>(id, model, init) {

  /** The hidden field carrying the PNG data URL. */
  val image: HiddenField<String> = HiddenField("image", Model(model.`object`))

  override fun addComponents() {
    outputMarkupId = true
    +image
  }

  override fun onBeforeRender() {
    image.modelObject = modelObject
    super.onBeforeRender()
  }

  override fun labelTarget(): FormComponent<*>? = null

  override fun convertInput() {
    convertedInput = image.convertedInput?.takeIf { it.startsWith("data:image/") }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-signature", "")
    tag.put("data-dm-signature-width", width.toString())
    tag.put("data-dm-signature-height", height.toString())
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-signature")

  override fun script(): ResourceReference = DmResources.js("dm-signature")
}
