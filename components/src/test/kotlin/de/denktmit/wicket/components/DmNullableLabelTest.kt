package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmNullableLabel
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmNullableLabelTest : WicketTestBase() {

  @Test
  fun `renders empty string for null model value`() {
    val label = DmNullableLabel("nl", modelOf { null as String? })

    assertThat(label.defaultModelObjectAsString).isEmpty()
  }

  @Test
  fun `onConfigure applies visibility callback and component tag hook`() {
    val label = DmNullableLabel("nl", modelOf { "x" })
    val tag = ComponentTag("span", XmlTag.TagType.OPEN)
    label.visible = { false }
    label.addOnComponentTag = { it.put("data-nullable", "yes") }

    invokeDeclared(label, "onConfigure")
    invokeDeclared(label, "onComponentTag", tag)

    assertThat(label.isVisible).isFalse()
    assertThat(tag.attributes.getString("data-nullable")).isEqualTo("yes")
  }
}
