package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmIntField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmIntFieldTest : WicketTestBase() {

  @Test
  fun `uses default size`() {
    val field = DmIntField("if", Model.of(1))

    assertThat(field.size).isEqualTo(100)
  }

  @Test
  fun `configure and converter are applied`() {
    val field = DmIntField("if", Model.of(1))
    field.visible = { false }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isFalse()
    assertThat(invokeDeclared(field, "createConverter", Int::class.java)?.javaClass?.simpleName).contains("IntegerConverter")
  }
}
