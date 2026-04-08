package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.form.DmButton
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class DmButtonTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val button = DmButton("btn")

    assertThat(button.id).isEqualTo("btn")
  }

  @Test
  fun `onSubmit invokes callback when present`() {
    var submitted = false
    val button = DmButton("btn", onSubmit = { submitted = true })

    invokeDeclared(button, "onSubmit")

    assertThat(submitted).isTrue()
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val button = DmButton("btn")
    invokeDeclared(button, "onInitialize")

    assertThat(button.behaviors).isNotEmpty
  }

  @Test
  fun `null onSubmit does not throw`() {
    val button = DmButton("btn")

    assertThatCode { invokeDeclared(button, "onSubmit") }.doesNotThrowAnyException()
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val button = DmButton("btn") {
      +DisabledBehavior()
    }
    invokeDeclared(button, "onInitialize")

    assertThat(button.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val button = DmButton("btn", init = { initCalled = true })

    invokeDeclared(button, "onInitialize")

    assertThat(initCalled).isTrue()
  }
}
