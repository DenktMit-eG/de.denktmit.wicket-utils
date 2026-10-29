package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.Component
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.util.tester.FormTester

/** Renders one input inside a form and submits it, the way every input of the catalogue is proven. */
abstract class InputTestBase : WidgetTestBase() {

  /** Renders [input] inside a form whose markup for the input is [inputMarkup]. */
  protected fun <C : Component> renderInForm(input: C, inputMarkup: String = "<div wicket:id=\"${input.id}\"></div>"): C {
    val form = Form<Unit>("form")
    form.add(input)
    render(form, "<form wicket:id=\"form\">$inputMarkup<input type=\"submit\" value=\"go\"/></form>")
    return input
  }

  /** A form tester for the rendered form. */
  protected fun form(): FormTester = tester.newFormTester("form")
}
