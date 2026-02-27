package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmForm
import org.assertj.core.api.Assertions.assertThat
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
}
