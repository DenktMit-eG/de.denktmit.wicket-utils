package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.form.DmBigDecimalField
import org.apache.wicket.model.IModel
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

  @Test
  fun `onInitialize adds CSS with filled model`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `onConfigure with filled model adds filled class`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))
    invokeDeclared(field, "onConfigure")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `componentTag callback is stored`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))
    var called = false
    field.componentTag = { called = true }
    field.componentTag(null)

    assertThat(called).isTrue()
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE)) {
      +DisabledBehavior()
    }
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onConfigure with visible callback true keeps visible`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))
    field.visible = { true }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE)) { initCalled = true }

    invokeDeclared(field, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with empty model adds CssClassRemover`() {
    val model: IModel<BigDecimal?> = Model()
    val field = DmBigDecimalField("bdf", model)

    invokeDeclared(field, "onConfigure")

    assertThat(field.behaviors).anyMatch { it.javaClass.simpleName == "CssClassRemover" }
  }

  @Test
  fun `onConfigure without visible callback keeps visibility`() {
    val field = DmBigDecimalField("bdf", Model.of(BigDecimal.ONE))
    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }
}
