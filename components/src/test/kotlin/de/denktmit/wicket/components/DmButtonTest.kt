package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmButton
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmButtonTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val button = DmButton("btn")

    assertThat(button.id).isEqualTo("btn")
  }

  @Test
  fun `onSubmit invokes callback when present`() {
    var submitted = false
    val button = DmButton("btn", onSubmit = { submitted = true })

    invokeDeclared(button, "onSubmit")

    assertThat(submitted).isTrue()
  }
}
