package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * A number input with up and down controls (requirement E.40). The controls step the native number input by
 * script; without script the input's own spinner and the keyboard do the same, so nothing is lost.
 */
open class DmSpinner<N>
@JvmOverloads
constructor(
  id: String,
  model: IModel<N?>,
  type: Class<N>,
  min: N? = null,
  max: N? = null,
  step: N? = null,
  init: DmInputPanel<N?>.() -> Unit = {},
) : DmInputPanel<N?>(id, model, init) where N : Number, N : Comparable<N> {

  /** The native number input. */
  val input: DmNumberField<N> = DmNumberField("input", model, type, min, max, step)

  override fun addComponents() {
    +input
    +WebMarkupContainer("down").apply { add(DmIcon("icon", "minus")) }
    +WebMarkupContainer("up").apply { add(DmIcon("icon", "plus")) }
  }

  override fun labelTarget(): FormComponent<*> = input

  override fun convertInput() {
    convertedInput = input.convertedInput
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-spinner", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-spinner")

  override fun script(): ResourceReference = DmResources.js("dm-spinner")
}
