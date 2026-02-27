package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmImage
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
}
