package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.form.DmIntField
import org.apache.wicket.model.IModel
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

  @Test
  fun `onInitialize adds CSS with filled model`() {
    val field = DmIntField("if", Model.of(1))
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `onConfigure with filled model adds filled class`() {
    val field = DmIntField("if", Model.of(1))
    invokeDeclared(field, "onConfigure")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `componentTag callback is stored`() {
    val field = DmIntField("if", Model.of(1))
    var called = false
    field.componentTag = { called = true }
    field.componentTag(null)

    assertThat(called).isTrue()
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val field = DmIntField("if", Model.of(1)) {
      +DisabledBehavior()
    }
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onConfigure with visible callback true keeps visible`() {
    val field = DmIntField("if", Model.of(1))
    field.visible = { true }

    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val field = DmIntField("if", Model.of(1)) { initCalled = true }

    invokeDeclared(field, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with empty model adds CssClassRemover`() {
    val model: IModel<Int> = Model()
    val field = DmIntField("if", model)

    invokeDeclared(field, "onConfigure")

    assertThat(field.behaviors).anyMatch { it.javaClass.simpleName == "CssClassRemover" }
  }

  @Test
  fun `onConfigure without visible callback keeps visibility`() {
    val field = DmIntField("if", Model.of(1))
    invokeDeclared(field, "onConfigure")

    assertThat(field.isVisible).isTrue()
  }
}
