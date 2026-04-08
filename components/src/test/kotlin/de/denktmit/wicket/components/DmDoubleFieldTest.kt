package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.form.DmDoubleField
import org.apache.wicket.model.IModel
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

  @Test
  fun `onInitialize adds CSS with filled model`() {
    val field = DmDoubleField("df", Model.of(1.0))
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `onConfigure with filled model adds filled class`() {
    val field = DmDoubleField("df", Model.of(1.0))
    invokeDeclared(field, "onConfigure")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `onConfigure without visible callback keeps visibility`() {
    val field = DmDoubleField("df", Model.of(1.0))
    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }

  @Test
  fun `componentTag callback is stored`() {
    val field = DmDoubleField("df", Model.of(1.0))
    var called = false
    field.componentTag = { called = true }
    field.componentTag(null)

    assertThat(called).isTrue()
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val field = DmDoubleField("df", Model.of(1.0)) {
      +DisabledBehavior()
    }
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onConfigure with visible callback true keeps visible`() {
    val field = DmDoubleField("df", Model.of(1.0))
    field.visible = { true }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val field = DmDoubleField("df", Model.of(1.0)) { initCalled = true }

    invokeDeclared(field, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with empty model adds CssClassRemover`() {
    val model: IModel<Double> = Model()
    val field = DmDoubleField("df", model)

    invokeDeclared(field, "onConfigure")

    assertThat(field.behaviors).anyMatch { it.javaClass.simpleName == "CssClassRemover" }
  }
}
