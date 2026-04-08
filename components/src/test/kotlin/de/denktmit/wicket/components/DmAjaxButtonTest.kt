package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxButton
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
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

  @Test
  fun `onInitialize adds CSS`() {
    val button = DmAjaxButton("ab")

    invokeDeclared(button, "onInitialize")

    assertThat(button.behaviors).isNotEmpty
  }

  @Test
  fun `null onSubmit does not throw`() {
    val button = DmAjaxButton("ab")

    assertThatCode {
      invokeDeclared(button, "onSubmit", mockk<AjaxRequestTarget>(relaxed = true))
    }.doesNotThrowAnyException()
  }

  @Test
  fun `operator unaryPlus for AbstractAjaxBehavior`() {
    val behavior = mockk<AbstractAjaxBehavior>(relaxed = true)
    val button = DmAjaxButton("ab") {
      +behavior
    }

    invokeDeclared(button, "onInitialize")

    assertThat(button.behaviors).contains(behavior)
  }
}
