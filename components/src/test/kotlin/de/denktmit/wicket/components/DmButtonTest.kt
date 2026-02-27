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
}
