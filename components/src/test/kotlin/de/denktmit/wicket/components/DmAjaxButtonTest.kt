package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxButton
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxButtonTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val button = DmAjaxButton("ab")

    assertThat(button.id).isEqualTo("ab")
  }
}
