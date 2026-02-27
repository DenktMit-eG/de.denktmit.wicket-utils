package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmBigDecimalField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DmBigDecimalFieldTest : WicketTestBase() {

  @Test
  fun `uses default size`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))

    assertThat(field.size).isEqualTo(100)
  }

  @Test
  fun `configure and converter are applied`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))
    field.visible = { false }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isFalse()
    assertThat(invokeDeclared(field, "createConverter", BigDecimal::class.java)?.javaClass?.simpleName).contains("BigDecimalConverter")
  }
}
