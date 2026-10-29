package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField
import org.apache.wicket.model.IModel
import org.apache.wicket.validation.IValidatable
import org.apache.wicket.validation.IValidator
import org.apache.wicket.validation.ValidationError

/**
 * A text with suggestions from a source (requirement E.43), a thin layer over Wicket's `AutoCompleteTextField`:
 * [source] answers the typed text with suggestions once [minLength] characters are typed, [delay] milliseconds
 * after the last key; with [freeText] off, a value the source does not offer is rejected on submit. Without script
 * the field is a plain text input and submits what was typed.
 */
open class DmCombo
@JvmOverloads
constructor(
  id: String,
  model: IModel<String?>?,
  @Transient
  private val source: (String) -> List<String>,
  /** Characters typed before the source is asked. */
  minLength: Int = 1,
  /** Milliseconds after the last key before the source is asked. */
  delay: Int = 300,
  /** Whether a value the source does not offer is accepted. */
  val freeText: Boolean = true,
  /** The most suggestions shown. */
  private val limit: Int = 10,
) : AutoCompleteTextField<String>(id, model, AutoCompleteSettings().setMinInputLength(minLength).setThrottleDelay(delay).setShowListOnEmptyInput(false)) {

  /** The suggestions for [input], at most `limit` of them. */
  public override fun getChoices(input: String): Iterator<String> = source(input).take(limit).iterator()

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    if (!freeText) {
      add(object : IValidator<String> {
        override fun validate(validatable: IValidatable<String>) {
          if (validatable.value !in source(validatable.value)) validatable.error(ValidationError().addKey("notOffered").setVariable("input", validatable.value))
        }
      })
    }
  }
}
