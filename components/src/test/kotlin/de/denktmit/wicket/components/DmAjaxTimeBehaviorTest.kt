package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxTimeBehavior
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class DmAjaxTimeBehaviorTest : WicketTestBase() {

  @Test
  fun `constructs with interval`() {
    val behavior = DmAjaxTimeBehavior(Duration.ofSeconds(1))

    assertThat(behavior).isNotNull
  }
}
