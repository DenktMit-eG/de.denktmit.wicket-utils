package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmImage
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.apache.wicket.request.resource.ByteArrayResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmImageTest : WicketTestBase() {

  @Test
  fun `constructs with id and resource`() {
    val image = DmImage("i", ByteArrayResource("image/png", byteArrayOf(1)))

    assertThat(image.id).isEqualTo("i")
  }

  @Test
  fun `reloadAntiCache updates anti cache and resource`() {
    val image = DmImage("i", ByteArrayResource("image/png", byteArrayOf(1)))

    image.reloadAntiCache(listOf("a", "b"), byteArrayOf(9))

    assertThat(image.antiCache).containsExactly("a", "b")
    assertThat(image.resource).isInstanceOf(ByteArrayResource::class.java)
    assertThat(invokeDeclared(image, "shouldAddAntiCacheParameter") as Boolean).isTrue()
  }

  @Test
  fun `onConfigure applies visibility callback`() {
    val image = DmImage("i", ByteArrayResource("image/png", byteArrayOf(1)))
    image.visibility = { false }

    invokeDeclared(image, "onConfigure")

    assertThat(image.isVisible).isFalse()
  }

  @Test
  fun `anti cache parameter branch and ajax flags are applied`() {
    val image = DmImage("i", ByteArrayResource("image/png", byteArrayOf(1)))
    val tag = ComponentTag("img", XmlTag.TagType.OPEN)
    tag.put("src", "/img.png")

    image.reloadAntiCache(listOf("a b"), byteArrayOf(5))
    invokeDeclared(image, "addAntiCacheParameter", tag)
    image.enableAjax()

    assertThat(tag.attributes.getString("src")).contains("v=a-b")
    assertThat(image.outputMarkupId).isTrue()
    assertThat(image.outputMarkupPlaceholderTag).isTrue()
  }
}
