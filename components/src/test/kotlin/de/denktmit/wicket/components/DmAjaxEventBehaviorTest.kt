package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxEventBehavior
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxEventBehaviorTest : WicketTestBase() {

  @Test
  fun `constructs with event behavior`() {
    val behavior = DmAjaxEventBehavior("click", eventBehavior = {})

    assertThat(behavior).isNotNull
  }

  @Test
  fun `onEvent invokes callback`() {
    var called = false
    val behavior = DmAjaxEventBehavior("click", eventBehavior = { called = true })

    invokeDeclared(behavior, "onEvent", mockk<AjaxRequestTarget>(relaxed = true))

    assertThat(called).isTrue()
  }

  @Test
  fun `init lambda is stored`() {
    var initCalled = false
    val behavior = DmAjaxEventBehavior("click", eventBehavior = {}, init = { initCalled = true })

    behavior.init.invoke(behavior)

    assertThat(initCalled).isTrue()
  }
}
