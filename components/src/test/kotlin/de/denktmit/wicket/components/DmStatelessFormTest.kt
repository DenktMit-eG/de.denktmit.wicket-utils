package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmStatelessForm
import io.mockk.mockk
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class DmStatelessFormTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val form = DmStatelessForm<String>("sf") {}

    assertThat(form.id).isEqualTo("sf")
  }

  @Test
  fun `submit and error callbacks can be invoked`() {
    var submitted = false
    var errored = false
    val form = DmStatelessForm<String>("sf") {}
    form.onSubmit = { submitted = true }
    form.onError = { errored = true }

    invokeDeclared(form, "onSubmit")
    invokeDeclared(form, "onError")

    assertThat(submitted).isTrue()
    assertThat(errored).isTrue()
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val form = DmStatelessForm<String>("sf") {}
    invokeDeclared(form, "onInitialize")

    assertThat(form.behaviors).isNotEmpty
  }

  @Test
  fun `null onSubmit and onError do not throw`() {
    val form = DmStatelessForm<String>("sf") {}

    assertThatCode { invokeDeclared(form, "onSubmit") }.doesNotThrowAnyException()
    assertThatCode { invokeDeclared(form, "onError") }.doesNotThrowAnyException()
  }

  @Test
  fun `operator unaryPlus for Component`() {
    val form = DmStatelessForm<String>("sf") {
      +Label("child", "x")
    }
    invokeDeclared(form, "onInitialize")

    assertThat(form.get("child")).isNotNull
  }

  @Test
  fun `operator unaryPlus for AbstractAjaxBehavior`() {
    val behavior = mockk<AbstractAjaxBehavior>(relaxed = true)
    val form = DmStatelessForm<String>("sf") {
      +behavior
    }
    invokeDeclared(form, "onInitialize")

    assertThat(form.behaviors).contains(behavior)
  }
}
