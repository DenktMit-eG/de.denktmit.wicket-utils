package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.AttributeModifier
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import org.apache.wicket.markup.html.form.FormComponent

/**
 * The common attributes and behaviours of a text-like input (requirement E.39): placeholder, maximum length,
 * pattern, and a live update of the page when the value changes (I.84). They apply to any `FormComponent`, the
 * toolbox's own and Wicket's.
 */
object InputAttributes

/** Shows [text] while the input is empty. */
fun <C : FormComponent<*>> C.placeholder(text: String): C = apply { add(AttributeModifier.replace("placeholder", text)) }

/** Limits the input to [length] characters in the browser and on the server. */
fun <C : FormComponent<String>> C.maxLength(length: Int): C = apply {
  add(AttributeModifier.replace("maxlength", length.toString()))
  add(org.apache.wicket.validation.validator.StringValidator.maximumLength(length) as org.apache.wicket.validation.IValidator<String>)
}

/** Requires the input to match [regex] in the browser and on the server. */
fun <C : FormComponent<String>> C.pattern(regex: String): C = apply {
  add(AttributeModifier.replace("pattern", regex))
  add(org.apache.wicket.validation.validator.PatternValidator(regex) as org.apache.wicket.validation.IValidator<String>)
}

/** Marks the input as required and announces it. */
fun <C : FormComponent<*>> C.required(): C = apply {
  isRequired = true
  add(AttributeModifier.replace("aria-required", "true"))
}

/**
 * Updates the model on every change and calls [onChange] with the Ajax target so the page can refresh what depends
 * on the value (requirements E.39, I.84). Without script the value still arrives with the form submit.
 */
fun <C : FormComponent<*>> C.liveUpdate(onChange: (AjaxRequestTarget) -> Unit): C = apply {
  add(object : OnChangeAjaxBehavior() {
    override fun onUpdate(target: AjaxRequestTarget) = onChange(target)
  })
}

/** Adds the stable hooks to a Wicket component that is not one of the toolbox's own. */
fun <C : FormComponent<*>> C.hooks(): C = apply { widgetHooks() }
