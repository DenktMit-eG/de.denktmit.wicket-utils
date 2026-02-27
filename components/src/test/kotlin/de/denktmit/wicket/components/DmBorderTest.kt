package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmBorder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmBorderTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val border = DmBorder("b")

    assertThat(border.id).isEqualTo("b")
  }
}
