package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmPasswordTextfield
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPasswordTextfieldTest : WicketTestBase() {

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
}
