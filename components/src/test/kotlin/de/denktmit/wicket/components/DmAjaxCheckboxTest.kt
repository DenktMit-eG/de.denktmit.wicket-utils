package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxCheckbox
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxCheckboxTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val checkbox = DmAjaxCheckbox("ac", Model.of(true))

    assertThat(checkbox.id).isEqualTo("ac")
  }

  @Test
  fun `onInitialize adds CSS`() {
    val checkbox = DmAjaxCheckbox("ac", Model.of(true))

    invokeDeclared(checkbox, "onInitialize")
  }

  @Test
  fun `onUpdate callback is invoked`() {
    var called = false
    val checkbox = DmAjaxCheckbox("ac", Model.of(true))
    checkbox.atUpdate = { called = true }

    invokeDeclared(checkbox, "onUpdate", mockk<AjaxRequestTarget>(relaxed = true))

    assertThat(called).isTrue()
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val checkbox = DmAjaxCheckbox("ac", Model.of(true)) { initCalled = true }

    invokeDeclared(checkbox, "onInitialize")

    assertThat(initCalled).isTrue()
  }
}
