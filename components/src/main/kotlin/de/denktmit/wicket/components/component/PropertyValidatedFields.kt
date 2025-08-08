package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.form.DmCheckbox
import de.denktmit.wicket.components.form.DmTextArea
import de.denktmit.wicket.components.form.DmTextField
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.markup.html.form.EmailTextField
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.model.IModel
import org.apache.wicket.validation.IValidatable
import org.apache.wicket.validation.IValidationError
import org.apache.wicket.validation.IValidator
import org.apache.wicket.validation.ValidationError
import org.apache.wicket.validation.validator.PatternValidator

fun <V : String?> createPropertyValidatedTextField(
  id: String,
  model: IModel<V?>? = null,
): DmTextField<V> =
  DmTextField(id, model) {
    val validators: List<IValidator<V>> = listOf(PropertyValidator())
    add(*validators.toTypedArray())
  }

fun createPropertyValidatedTextArea(
  id: String,
  model: IModel<String>,
): DmTextArea<String> =
  DmTextArea(id, model) {
    val validators: List<IValidator<String>> = listOf(PropertyValidator())
    add(*validators.toTypedArray())
  }

fun createPropertyValidatedEmailField(
  id: String,
  model: IModel<String>? = null,
): EmailTextField =
  EmailTextField(id, model).apply {
    val validators: List<IValidator<String>> = listOf(PropertyValidator())
    add(*validators.toTypedArray())
  }

fun <V : String?> createPatternValidatedField(
  id: String,
  model: IModel<V?>? = null,
  pattern: String,
  errorMessage: String? = null,
): DmTextField<V> =
  DmTextField(id, model) {
    add(
      object : PatternValidator(pattern) {
        override fun decorate(
          error: IValidationError?,
          validatable: IValidatable<String>?,
        ): IValidationError {
          if (errorMessage != null) {
            return ValidationError(errorMessage)
          }
          return error ?: ValidationError("Invalid input")
        }
      },
    )
  }

fun createPropertyValidatedCheckBox(
  id: String,
  model: IModel<Boolean>,
): DmCheckbox =
  DmCheckbox(id, model) {
    val validators: List<IValidator<Boolean>> = listOf(PropertyValidator())
    add(*validators.toTypedArray())
  }

fun <T> createPropertyValidatedNumberTextField(
  id: String,
  model: IModel<T>? = null,
): NumberTextField<T>
    where T : Number, T : Comparable<T> =
  NumberTextField<T>(id, model).apply {
    val validators: List<IValidator<T>> = listOf(PropertyValidator())
    add(*validators.toTypedArray())
  }
