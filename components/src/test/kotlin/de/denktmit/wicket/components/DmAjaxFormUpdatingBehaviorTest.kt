package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DmAjaxFormUpdatingBehavior
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxFormUpdatingBehaviorTest : WicketTestBase() {

  @Test
  fun `onUpdate invokes callback when present`() {
    var invoked = false
    val behavior = DmAjaxFormUpdatingBehavior(onUpdate = { invoked = true })

    invokeDeclared(behavior, "onUpdate", mockk<AjaxRequestTarget>(relaxed = true))

    assertThat(invoked).isTrue()
  }

  @Test
  fun `onUpdate handles missing callback`() {
    val behavior = DmAjaxFormUpdatingBehavior()

    invokeDeclared(behavior, "onUpdate", null)

    assertThat(behavior).isNotNull
  }
}
