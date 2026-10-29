package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmNumberFieldsTest : InputTestBase() {

  @Test
  fun `long field carries min, max and step and reads a value back`() {
    val model = Model<Long?>(5L)
    val field = renderInForm(DmLongField("n", model, min = 0, max = 100, step = 5), "<input wicket:id=\"n\" type=\"number\"/>")
    assertHooks(field)
    assertThat(tag("n").getAttribute("min")).isEqualTo("0")
    assertThat(tag("n").getAttribute("max")).isEqualTo("100")
    assertThat(tag("n").getAttribute("step")).isEqualTo("5")
    assertThat(tag("n").getAttribute("inputmode")).isEqualTo("numeric")
    form().apply { setValue("n", "40"); submit() }
    assertThat(model.`object`).isEqualTo(40L)
  }

  @Test
  fun `a value outside the range is rejected on the server`() {
    val model = Model<Long?>(5L)
    renderInForm(DmLongField("n", model, max = 10), "<input wicket:id=\"n\" type=\"number\"/>")
    form().apply { setValue("n", "40"); submit() }
    assertThat(model.`object`).isEqualTo(5L)
    tester.assertErrorMessages("The value of 'n' must be at most 10.")
  }

  @Test
  fun `spinner wraps a number input with controls and submits through it`() {
    val model = Model<Int?>(2)
    val spinner = renderInForm(DmSpinner("qty", model, Int::class.javaObjectType, min = 1, max = 9, step = 1))
    assertHooks(spinner)
    assertThat(tag("qty").getAttribute("data-dm-spinner")).isNotNull
    assertThat(tag("input").getAttribute("type")).isEqualTo("number")
    assertThat(tag("input").getAttribute("value")).isEqualTo("2")
    assertThat(tag("up").getAttribute("aria-label")).isEqualTo("Increase")
    assertThat(spinner.labelTarget()).isSameAs(spinner.input)
    assertStyleSheet("dm-spinner")
    assertScript("dm-spinner")
    form().apply { setValue("qty:input", "7"); submit() }
    assertThat(model.`object`).isEqualTo(7)
  }
}
