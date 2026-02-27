package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmExternalLink
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmExternalLinkTest : WicketTestBase() {

  @Test
  fun `constructs with href and label`() {
    val link = DmExternalLink("e", "https://example.org", "label")

    assertThat(link.defaultModelObjectAsString).isEqualTo("https://example.org")
  }

  @Test
  fun `onComponentTag enforces blank target`() {
    val link = DmExternalLink("e", "https://example.org", "label")
    val tag = ComponentTag("a", XmlTag.TagType.OPEN)

    invokeDeclared(link, "onComponentTag", tag)

    assertThat(tag.attributes.getString("target")).isEqualTo("_blank")
  }
}
