package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmWebSocketBehavior
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmWebSocketBehaviorTest : WicketTestBase() {

  @Test
  fun `constructs with defaults`() {
    val behavior = DmWebSocketBehavior()

    assertThat(behavior).isNotNull
  }
}
