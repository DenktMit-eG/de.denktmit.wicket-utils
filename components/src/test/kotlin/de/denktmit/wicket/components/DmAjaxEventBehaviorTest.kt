package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxEventBehavior
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxEventBehaviorTest : WicketTestBase() {

  @Test
  fun `constructs with event behavior`() {
    val behavior = DmAjaxEventBehavior("click", eventBehavior = {})

    assertThat(behavior).isNotNull
  }
}
