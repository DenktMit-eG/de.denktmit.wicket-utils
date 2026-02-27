package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxButton
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxButtonTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val button = DmAjaxButton("ab")

    assertThat(button.id).isEqualTo("ab")
  }

  @Test
  fun `onSubmit invokes callback`() {
    var submitted = false
    val button = DmAjaxButton("ab")
    button.onSubmit = { submitted = true }

    invokeDeclared(button, "onSubmit", mockk<AjaxRequestTarget>(relaxed = true))
    assertThat(submitted).isTrue()
  }
}
