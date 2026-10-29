package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.Side
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A check box with an indeterminate state (requirement E.47): the model is `true`, `false` or `null`. Script cycles
 * the box through the three states and records the state in a hidden field; without script the box is a plain
 * two-state check box and `null` reads as unchecked, which is what the browser can express.
 */
open class DmTriStateCheckbox
@JvmOverloads
constructor(
  id: String,
  model: IModel<Boolean?>,
  label: IModel<String>,
  var labelSide: Side = Side.END,
  init: DmInputPanel<Boolean?>.() -> Unit = {},
) : DmInputPanel<Boolean?>(id, model, init) {

  @JvmOverloads
  constructor(id: String, model: IModel<Boolean?>, label: String, labelSide: Side = Side.END, init: DmInputPanel<Boolean?>.() -> Unit = {}) :
    this(id, model, Model.of(label), labelSide, init)

  private val labelModel = label

  /** The native check box, checked while the model is `true`. */
  val input: CheckBox = object : CheckBox("input", Model.of(model.`object` == true)) {
    override fun onBeforeRender() {
      modelObject = this@DmTriStateCheckbox.modelObject == true
      super.onBeforeRender()
    }

    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("aria-checked", if (this@DmTriStateCheckbox.modelObject == null) "mixed" else this@DmTriStateCheckbox.modelObject.toString())
      if (this@DmTriStateCheckbox.modelObject == null) tag.put("data-dm-indeterminate", "")
    }
  }

  /** Where script records the state: `true`, `false`, `null`, or empty when script never touched it. */
  val state: HiddenField<String> = HiddenField("state", Model.of(""))

  override fun addComponents() {
    +input
    +state
    +Label("label", labelModel)
  }

  override fun labelTarget(): FormComponent<*> = input

  override fun convertInput() {
    convertedInput = when (state.convertedInput) {
      "true" -> true
      "false" -> false
      "null" -> null
      else -> input.convertedInput ?: false
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-tristate", "")
    tag.append("class", "dm-switch-label-${labelSide.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-switch")

  override fun script(): ResourceReference = DmResources.js("dm-tri-state-checkbox")
}
