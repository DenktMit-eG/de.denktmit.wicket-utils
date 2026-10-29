package de.denktmit.wicket.components.input

import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator
import org.apache.wicket.markup.html.form.validation.IFormValidator
import org.apache.wicket.validation.IValidatable
import org.apache.wicket.validation.IValidator
import org.apache.wicket.validation.ValidationError
import org.apache.wicket.validation.validator.EmailAddressValidator
import org.apache.wicket.validation.validator.PatternValidator
import org.apache.wicket.validation.validator.RangeValidator
import org.apache.wicket.validation.validator.StringValidator
import org.apache.wicket.validation.validator.UrlValidator
import java.io.Serializable

/**
 * The validation rules of the catalogue (requirement E.58): required, length, range, pattern, e-mail, URL, date
 * order and a custom rule. Wicket's own validators carry messages in English and German already; the toolbox's
 * own ([CustomValidator], [DateOrderValidator], [DisabledDatesValidator]) bring theirs next to the class. Every
 * message lands at the input it belongs to, which [DmFormField] shows. Form-level rules ([DateOrderValidator]) resolve
 * their text through the application: install the toolbox's bundle with [de.denktmit.wicket.components.resource.DmMessages.install]
 * to get the translated message, else the English default is used.
 */
object DmValidators {
  /** The input must be between [min] and [max] characters. */
  @JvmStatic
  fun length(min: Int, max: Int): IValidator<String> = StringValidator.lengthBetween(min, max)

  /** The input must have at least [min] characters. */
  @JvmStatic
  fun minLength(min: Int): IValidator<String> = StringValidator.minimumLength(min)

  /** The input must have at most [max] characters. */
  @JvmStatic
  fun maxLength(max: Int): IValidator<String> = StringValidator.maximumLength(max)

  /** The value must lie between [min] and [max]. */
  @JvmStatic
  fun <T> range(min: T, max: T): IValidator<T> where T : Comparable<T>, T : Serializable = RangeValidator.range(min, max)

  /** The input must match [regex]. */
  @JvmStatic
  fun pattern(regex: String): IValidator<String> = PatternValidator(regex)

  /** The input must be an e-mail address. */
  @JvmStatic
  fun email(): IValidator<String> = EmailAddressValidator.getInstance()

  /** The input must be a URL. */
  @JvmStatic
  fun url(): IValidator<String> = UrlValidator()

  /** A rule of the page author's own: [rule] returns `null` for valid or a message for invalid. */
  @JvmStatic
  fun <T> custom(rule: (T) -> String?): IValidator<T> = CustomValidator(rule)

  /** The [from] value must not be after the [to] value; attach the result to the form. */
  @JvmStatic
  fun <T : Comparable<T>> dateOrder(from: FormComponent<T>, to: FormComponent<T>): IFormValidator = DateOrderValidator(from, to)
}

/** Validates with the author's [rule]; a returned message is the error, shown as it is. */
class CustomValidator<T>(
  @Transient
  private val rule: (T) -> String?,
) : IValidator<T> {
  override fun validate(validatable: IValidatable<T>) {
    rule(validatable.value)?.let { validatable.error(ValidationError(it)) }
  }
}

/** Fails the form when the value of [from] is after the value of [to]; the message lands at [to]. */
class DateOrderValidator<T : Comparable<T>>(
  private val from: FormComponent<T>,
  private val to: FormComponent<T>,
) : AbstractFormValidator() {
  override fun getDependentFormComponents(): Array<FormComponent<*>> = arrayOf(from, to)

  override fun validate(form: Form<*>) {
    val start = from.convertedInput
    val end = to.convertedInput
    if (start != null && end != null && start > end) {
      val fromName = from.label?.`object` ?: from.id
      val toName = to.label?.`object` ?: to.id
      to.error(ValidationError().addKey("DateOrderValidator").setVariable("from", fromName).setVariable("to", toName).setMessage("$toName must not be before $fromName."))
    }
  }
}
