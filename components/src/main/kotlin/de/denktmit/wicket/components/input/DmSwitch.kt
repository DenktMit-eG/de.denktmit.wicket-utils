package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.Side
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A boolean as a toggle switch (requirement E.47): a native check box with `role="switch"` inside its label, the
 * label on either [labelSide]; the switch look is CSS only, so it submits and toggles without script.
 */
open class DmSwitch
@JvmOverloads
constructor(
  id: String,
  model: IModel<Boolean>,
  label: IModel<String>,
  var labelSide: Side = Side.END,
  init: DmInputPanel<Boolean>.() -> Unit = {},
) : DmInputPanel<Boolean>(id, model, init) {

  @JvmOverloads
  constructor(id: String, model: IModel<Boolean>, label: String, labelSide: Side = Side.END, init: DmInputPanel<Boolean>.() -> Unit = {}) :
    this(id, model, Model.of(label), labelSide, init)

  private val labelModel = label

  /** The native check box. */
  val input: CheckBox = object : CheckBox("input", model) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("role", "switch")
      tag.put("aria-checked", (modelObject == true).toString())
    }
  }

  override fun addComponents() {
    +input
    +Label("label", labelModel)
  }

  override fun labelTarget(): FormComponent<*> = input

  override fun convertInput() {
    convertedInput = input.convertedInput ?: false
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("class", "dm-switch-label-${labelSide.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-switch")
}
