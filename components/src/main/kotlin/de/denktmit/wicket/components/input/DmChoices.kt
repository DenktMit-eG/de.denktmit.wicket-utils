package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.choice.DmChoiceRenderer
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.markup.html.form.ListMultipleChoice
import org.apache.wicket.markup.html.form.RadioChoice
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.string.Strings

/**
 * A list of many (requirement E.42): a native `select multiple` over [choices] with a [renderer] for the shown text,
 * the model a collection of the chosen values.
 */
open class DmMultiSelect<T>
@JvmOverloads
constructor(
  id: String,
  model: IModel<out Collection<T>>,
  choices: IModel<out List<T>>,
  renderer: IChoiceRenderer<in T> = DmChoiceRenderer(),
  /** The visible rows. */
  rows: Int = 6,
) : ListMultipleChoice<T>(id, model, choices, renderer) {
  init {
    setMaxRows(rows)
  }

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }
}

/** A check box group (requirement E.42): one check box per choice, the model a collection of the chosen values. */
open class DmCheckGroup<T>
@JvmOverloads
constructor(
  id: String,
  model: IModel<out Collection<T>>,
  choices: IModel<out List<T>>,
  renderer: IChoiceRenderer<in T> = DmChoiceRenderer(),
) : CheckBoxMultipleChoice<T>(id, model, choices, renderer) {
  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    prefix = "<span class=\"dm-choice\">"
    suffix = "</span>"
  }
}

/**
 * A radio group of one (requirement E.42): one radio per choice, an optional empty choice ([nullValid]) that
 * renders as a radio for "none", the model the chosen value.
 */
open class DmRadioChoice<T>
@JvmOverloads
constructor(
  id: String,
  model: IModel<T?>,
  choices: IModel<out List<T>>,
  renderer: IChoiceRenderer<in T> = DmChoiceRenderer(),
  /** The label of the empty choice; `null` for no empty choice. */
  private val emptyChoice: IModel<String>? = null,
) : RadioChoice<T>(id, model, choices, renderer) {

  @JvmOverloads
  constructor(id: String, model: IModel<T?>, choices: List<T>, renderer: IChoiceRenderer<in T> = DmChoiceRenderer(), emptyChoice: String? = null) :
    this(id, model, Model.ofList(choices), renderer, emptyChoice?.let { Model.of(it) })

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    prefix = "<span class=\"dm-choice\">"
    suffix = "</span>"
  }

  override fun onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    super.onComponentTagBody(markupStream, openTag)
    if (emptyChoice != null) {
      val id = markupId + "-none"
      val checked = if (modelObject == null && !hasRawInput()) " checked=\"checked\"" else ""
      response.write(
        "<span class=\"dm-choice dm-choice-empty\"><input name=\"$inputName\" type=\"radio\" value=\"-1\" id=\"$id\"$checked/>" +
          "<label for=\"$id\">${Strings.escapeMarkup(emptyChoice.`object`)}</label></span>",
      )
    }
  }
}
