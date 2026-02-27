package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmDoubleField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmDoubleFieldTest : WicketTestBase() {

  @Test
  fun `uses default size`() {
    val field = DmDoubleField("df", Model.of(1.0))

    assertThat(field.size).isEqualTo(100)
  }

  @Test
  fun `configure and converter are applied`() {
    val field = DmDoubleField("df", Model.of(1.0))
    field.visible = { false }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isFalse()
    assertThat(invokeDeclared(field, "createConverter", Double::class.java)?.javaClass?.simpleName).contains("DoubleConverter")
  }
}
