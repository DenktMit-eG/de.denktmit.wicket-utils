package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmStatelessForm
import org.assertj.core.api.Assertions.assertThat
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
}
