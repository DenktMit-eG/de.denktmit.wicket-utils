package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmRadioGroup
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmRadioGroupTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val radio = DmRadioGroup<String>("r")

    assertThat(radio.id).isEqualTo("r")
  }

  @Test
  fun `onConfigure applies visible callback`() {
    val radio = DmRadioGroup<String>("r")
    radio.visible = { false }

    invokeDeclared(radio, "onConfigure")

    assertThat(radio.isVisible).isFalse()
  }

  @Test
  fun `onInitialize adds CSS`() {
    val radio = DmRadioGroup<String>("r")

    invokeDeclared(radio, "onInitialize")
  }

  @Test
  fun `onComponentTag delegates to callback`() {
    val radio = DmRadioGroup<String>("r")
    radio.addOnComponentTag = { it.put("data-test", "ok") }
    val xmlTag = XmlTag()
    xmlTag.name = "div"
    xmlTag.type = XmlTag.TagType.OPEN
    val tag = ComponentTag(xmlTag)
    tag.id = "r"

    invokeDeclared(radio, "onComponentTag", tag)

    assertThat(tag.getAttribute("data-test")).isEqualTo("ok")
  }

  @Test
  fun `operator unaryPlus adds behavior`() {
    val radio = DmRadioGroup<String>("r") {
      +DisabledBehavior()
    }

    invokeDeclared(radio, "onInitialize")

    assertThat(radio.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val radio = DmRadioGroup<String>("r") { initCalled = true }

    invokeDeclared(radio, "onInitialize")

    assertThat(initCalled).isTrue()
  }
}
