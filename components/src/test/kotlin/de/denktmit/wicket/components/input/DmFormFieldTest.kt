package de.denktmit.wicket.components.input

import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmFormFieldTest : InputTestBase() {

  private val markup = "<div wicket:id=\"name\"><input wicket:id=\"input\" type=\"text\"/></div>"

  @Test
  fun `binds the label, shows help, marks required and places the error at the input`() {
    val model = Model("")
    val field = DmFormField("name", "Name", "Family name first")
    field.add(TextField("input", model).required().maxLength(5))
    renderInForm(field, markup)
    assertHooks(field)
    assertThat(tag("label").getAttribute("for")).isEqualTo(tag("input").getAttribute("id"))
    assertThat(tag("help").value).isEqualTo("Family name first")
    assertThat(tag("input").getAttribute("aria-describedby")).isEqualTo(tag("help").getAttribute("id"))
    assertThat(tag("input").getAttribute("aria-required")).isEqualTo("true")
    assertThat(tag("name").getAttribute("class")).contains("dm-form-field-required")
    assertThat(tester.getTagByWicketId("error")).isNull()
    assertStyleSheet("dm-form-field")

    form().apply { setValue("name:name_body:input", "too long"); submit() }
    assertThat(tag("error").value).contains("Name").contains("maximum of 5")
    assertThat(tag("error").getAttribute("role")).isEqualTo("alert")
    assertThat(tag("input").getAttribute("aria-invalid")).isEqualTo("true")
    assertThat(tag("input").getAttribute("aria-describedby")).contains(tag("error").getAttribute("id"))
    assertThat(tag("name").getAttribute("class")).contains("dm-form-field-invalid")
  }

  @Test
  fun `a composite input is labelled through its inner input`() {
    val field = DmFormField("name", "Notify")
    val switch = DmSwitch("input", Model(false), "yes")
    field.add(switch)
    renderInForm(field, "<div wicket:id=\"name\"><div wicket:id=\"input\"></div></div>")
    assertThat(tag("label", 0).getAttribute("for")).isEqualTo(switch.input.markupId)
  }

  @Test
  fun `validators from the catalogue report in the user's language`() {
    tester.session.locale = java.util.Locale.GERMANY
    val field = DmFormField("name", "E-Mail")
    field.add(TextField("input", Model("")).apply { add(DmValidators.email()) })
    renderInForm(field, markup)
    form().apply { setValue("name:name_body:input", "nope"); submit() }
    assertThat(tag("error").value).contains("E-Mail")
  }

  @Test
  fun `custom rule and date order`() {
    val from = DmDateField("from", Model(java.time.LocalDate.of(2026, 5, 1)))
    val to = DmDateField("to", Model(java.time.LocalDate.of(2026, 4, 1))).apply { add(DmValidators.custom { if (it.year < 2000) "too early" else null }) }
    val form = org.apache.wicket.markup.html.form.Form<Unit>("form")
    form.add(from, to)
    form.add(DmValidators.dateOrder(from, to))
    render(form, "<form wicket:id=\"form\"><input wicket:id=\"from\"/><input wicket:id=\"to\"/></form>")
    tester.newFormTester("form").apply { setValue("from", "2026-05-01"); setValue("to", "2026-04-01"); submit() }
    assertThat(tester.getMessages(org.apache.wicket.feedback.FeedbackMessage.ERROR).map { it.toString() }).containsExactly("to must not be before from.")

    tester.clearFeedbackMessages()
    tester.newFormTester("form").apply { setValue("from", "2026-05-01"); setValue("to", "1999-04-01"); submit() }
    tester.assertErrorMessages("too early")
  }
}
