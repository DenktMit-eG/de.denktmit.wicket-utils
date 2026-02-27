package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmLabel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmLabelTest : WicketTestBase() {

  @Test
  fun `constructs with model`() {
    val label = DmLabel("l", Model.of("x"))

    assertThat(label.defaultModelObjectAsString).isEqualTo("x")
  }

  @Test
  fun `onConfigure applies visible callback and custom component tag hook`() {
    val label = DmLabel("l", Model.of("x"))
    val tag = ComponentTag("span", XmlTag.TagType.OPEN)
    label.visible = { false }
    label.addOnComponentTag = { it.put("data-test", "ok") }

    invokeDeclared(label, "onConfigure")
    invokeDeclared(label, "onComponentTag", tag)

    assertThat(label.isVisible).isFalse()
    assertThat(tag.attributes.getString("data-test")).isEqualTo("ok")
  }
}
