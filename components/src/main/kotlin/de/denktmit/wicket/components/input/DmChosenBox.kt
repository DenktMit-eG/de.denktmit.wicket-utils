package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.choice.DmChoiceRenderer
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.markup.html.form.ListMultipleChoice
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * Many values from a source shown as removable chips, with suggestions while typing (requirement E.45). The form
 * reads a native `select multiple`, which is what submits; script hides it and shows the chosen values as chips
 * with a text input that suggests from the remaining options.
 */
open class DmChosenBox<T>
@JvmOverloads
constructor(
  id: String,
  model: IModel<out Collection<T>>,
  choices: IModel<out List<T>>,
  renderer: IChoiceRenderer<in T> = DmChoiceRenderer(),
  val placeholder: IModel<String> = org.apache.wicket.model.Model.of("Add"),
  init: DmInputPanel<Collection<T>>.() -> Unit = {},
) : DmInputPanel<Collection<T>>(id, @Suppress("UNCHECKED_CAST") (model as IModel<Collection<T>>), init) {

  /** The native multiple select the form reads. */
  val select: ListMultipleChoice<T> = ListMultipleChoice("select", model, choices, renderer)

  override fun addComponents() {
    +select
  }

  override fun labelTarget(): FormComponent<*> = select

  override fun convertInput() {
    convertedInput = select.convertedInput ?: emptyList()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-chosen", "")
    tag.put("data-dm-chosen-placeholder", placeholder.`object`)
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-chosen-box")

  override fun script(): ResourceReference = DmResources.js("dm-chosen-box")
}
