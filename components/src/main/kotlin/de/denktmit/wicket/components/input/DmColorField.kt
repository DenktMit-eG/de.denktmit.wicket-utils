package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A colour value (requirement E.50): the browser's own picker (`input type="color"`) next to a text input for the
 * hex value; script keeps the two in step, and on submit the text wins when it is a valid `#rrggbb`, otherwise the
 * picker. The model holds the lower-case hex value or `null`.
 */
open class DmColorField
@JvmOverloads
constructor(
  id: String,
  model: IModel<String?>,
  init: DmInputPanel<String?>.() -> Unit = {},
) : DmInputPanel<String?>(id, model, init) {

  /** The native colour picker. */
  val picker: TextField<String> = object : TextField<String>("picker", Model(model.`object` ?: "#000000")) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("type", "color")
    }

    override fun getInputTypes(): Array<String> = arrayOf("color")
  }

  /** The hex text input. */
  val text: TextField<String> = TextField("text", Model(model.`object`))

  override fun addComponents() {
    +picker
    +text.placeholder("#rrggbb").apply { add(org.apache.wicket.AttributeModifier.replace("pattern", "#[0-9a-fA-F]{6}")) }
  }

  override fun onBeforeRender() {
    picker.modelObject = modelObject ?: "#000000"
    text.modelObject = modelObject
    super.onBeforeRender()
  }

  override fun labelTarget(): FormComponent<*> = text

  override fun convertInput() {
    val typed = text.convertedInput?.trim()?.lowercase()
    convertedInput = when {
      typed != null && HEX.matches(typed) -> typed
      typed.isNullOrEmpty() -> null
      else -> picker.convertedInput?.lowercase()
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-color", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-color-field")

  override fun script(): ResourceReference = DmResources.js("dm-color-field")

  companion object {
    private val HEX = Regex("#[0-9a-f]{6}")
  }
}
