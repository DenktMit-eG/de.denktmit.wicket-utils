package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.form.DmPasswordTextfield
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPasswordTextfieldTest : WicketTestBase() {

  private class Holder(var pw: String? = "secret")

  @Test
  fun `uses default size`() {
    val field = DmPasswordTextfield("pwd", Model.of("x"))

    assertThat(field.size).isEqualTo(100)
  }

  @Test
  fun `visible callback is applied on configure`() {
    val field = DmPasswordTextfield("pwd", Model.of("x"))
    field.visible = { false }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isFalse()
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val field = DmPasswordTextfield("pwd", Model.of("x"))
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `secondary KProperty constructor`() {
    val holder = Holder()
    val field = DmPasswordTextfield(holder::pw)

    assertThat(field.id).isEqualTo("pw")
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val field = DmPasswordTextfield("pwd", Model.of("x")) {
      +DisabledBehavior()
    }
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onConfigure without visible callback does not change visibility`() {
    val field = DmPasswordTextfield("pwd", Model.of("x"))
    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }
}
