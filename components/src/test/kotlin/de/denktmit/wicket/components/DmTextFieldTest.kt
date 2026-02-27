package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmTextField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTextFieldTest : WicketTestBase() {

  @Test
  fun `uses default size`() {
    val field = DmTextField<String>("tf", Model.of("x"))

    assertThat(field.size).isEqualTo(100)
  }

  @Test
  fun `configure toggles visible and filled branches`() {
    val filled = DmTextField<String>("tf", Model.of("x"))
    filled.visible = { false }
    invokeDeclared(filled, "onConfigure")

    val empty = DmTextField<String>("tf2", Model.of(""))
    invokeDeclared(empty, "onConfigure")

    assertThat(filled.isVisible).isFalse()
    assertThat(filled.behaviors).isNotEmpty
    assertThat(empty.behaviors).isNotEmpty
  }
}
