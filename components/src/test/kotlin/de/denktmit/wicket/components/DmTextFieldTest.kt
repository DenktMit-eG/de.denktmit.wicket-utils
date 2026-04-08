package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.form.DmTextField
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTextFieldTest : WicketTestBase() {

  private class Holder(var name: String? = "x")

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

  @Test
  fun `onInitialize adds CSS classes for filled model`() {
    val field = DmTextField<String>("tf", Model.of("x"))
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `onInitialize with blank model skips filled class`() {
    val field = DmTextField<String>("tf", Model.of(""))
    invokeDeclared(field, "onInitialize")

    assertThat(field.id).isEqualTo("tf")
  }

  @Test
  fun `onComponentTag delegates to callback`() {
    val field = DmTextField<String>("tf", Model.of("x"))
    val tag = ComponentTag("input", XmlTag.TagType.OPEN)
    field.componentTag = { it?.put("data-custom", "yes") }

    invokeDeclared(field, "onComponentTag", tag)

    assertThat(tag.attributes.getString("data-custom")).isEqualTo("yes")
  }

  @Test
  fun `secondary KProperty constructor`() {
    val holder = Holder()
    val field = DmTextField(holder::name)

    assertThat(field.id).isEqualTo("name")
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val field = DmTextField<String>("tf", Model.of("x")) {
      +DisabledBehavior()
    }
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).anyMatch { it is DisabledBehavior }
  }
}
