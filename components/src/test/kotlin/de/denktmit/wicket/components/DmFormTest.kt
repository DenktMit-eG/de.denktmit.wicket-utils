package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmForm
import io.mockk.mockk
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class DmFormTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val form = DmForm<String>("form") {}

    assertThat(form.id).isEqualTo("form")
  }

  @Test
  fun `invokes submit callback`() {
    var submitted = false
    val form = DmForm<String>("f") {}
    form.onSubmit = { submitted = true }

    invokeDeclared(form, "onSubmit")

    assertThat(submitted).isTrue()
  }

  @Test
  fun `configure and error callback are applied`() {
    var errored = false
    val form = DmForm<String>("f") {}
    form.visible = { false }
    form.onError = { errored = true }

    invokeDeclared(form, "onConfigure")
    invokeDeclared(form, "onError")

    assertThat(form.isVisible).isFalse()
    assertThat(errored).isTrue()
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val form = DmForm<String>("f") {}
    invokeDeclared(form, "onInitialize")

    assertThat(form.behaviors).isNotEmpty
  }

  @Test
  fun `null onSubmit and onError do not throw`() {
    val form = DmForm<String>("f") {}

    assertThatCode { invokeDeclared(form, "onSubmit") }.doesNotThrowAnyException()
    assertThatCode { invokeDeclared(form, "onError") }.doesNotThrowAnyException()
  }

  @Test
  fun `null visible callback does not change visibility`() {
    val form = DmForm<String>("f") {}

    invokeDeclared(form, "onConfigure")

    assertThat(form.isVisible).isTrue()
  }

  @Test
  fun `operator unaryPlus for Component`() {
    val form = DmForm<String>("f") {
      +Label("child", "x")
    }
    invokeDeclared(form, "onInitialize")

    assertThat(form.get("child")).isNotNull
  }

  @Test
  fun `operator unaryPlus for AbstractAjaxBehavior`() {
    val behavior = mockk<AbstractAjaxBehavior>(relaxed = true)
    val form = DmForm<String>("f") {
      +behavior
    }
    invokeDeclared(form, "onInitialize")

    assertThat(form.behaviors).contains(behavior)
  }
}
