package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmHidden
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmHiddenTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val hidden = DmHidden("h")

    assertThat(hidden.id).isEqualTo("h")
  }
}
