package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxTimeBehavior
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class DmAjaxTimeBehaviorTest : WicketTestBase() {

  @Test
  fun `constructs with interval`() {
    val behavior = DmAjaxTimeBehavior(Duration.ofSeconds(1))

    assertThat(behavior).isNotNull
  }

  @Test
  fun `timer invokes action when set and skips when missing`() {
    val behavior = DmAjaxTimeBehavior(Duration.ofSeconds(1))
    var called = false
    behavior.onAction = { called = true }

    invokeDeclared(behavior, "onTimer", mockk<AjaxRequestTarget>(relaxed = true))
    behavior.onAction = null
    invokeDeclared(behavior, "onTimer", null)

    assertThat(called).isTrue()
  }
}
