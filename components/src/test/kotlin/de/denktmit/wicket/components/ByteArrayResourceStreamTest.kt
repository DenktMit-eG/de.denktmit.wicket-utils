package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.ByteArrayResourceStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class ByteArrayResourceStreamTest {

  @Test
  fun `returns content type from constructor`() {
    val stream = ByteArrayResourceStream("hello".toByteArray(), "text/plain")
    assertThat(stream.contentType).isEqualTo("text/plain")
  }

  @Test
  fun `returns correct length`() {
    val data = "hello world".toByteArray()
    val stream = ByteArrayResourceStream(data, "text/plain")
    assertThat(stream.length().bytes()).isEqualTo(data.size.toLong())
  }

  @Test
  fun `returns input stream with correct data`() {
    val data = "test data".toByteArray()
    val stream = ByteArrayResourceStream(data, "application/octet-stream")
    val result = stream.inputStream.readAllBytes()
    assertThat(result).isEqualTo(data)
  }

  @Test
  fun `lastModifiedTime returns non-null instant`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    assertThat(stream.lastModifiedTime()).isNotNull
  }

  @Test
  fun `close does not throw`() {
    val stream = ByteArrayResourceStream("data".toByteArray(), "text/plain")
    stream.close()
  }

  @Test
  fun `locale defaults and can be set`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    assertThat(stream.locale).isNotNull

    stream.setLocale(Locale.FRENCH)
    assertThat(stream.locale).isEqualTo(Locale.FRENCH)
  }

  @Test
  fun `setLocale with null does not change locale`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    val original = stream.locale
    stream.setLocale(null)
    assertThat(stream.locale).isEqualTo(original)
  }

  @Test
  fun `style defaults to empty and can be set`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    assertThat(stream.style).isEmpty()

    stream.setStyle("bold")
    assertThat(stream.style).isEqualTo("bold")
  }

  @Test
  fun `setStyle with null does not change style`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    stream.setStyle("original")
    stream.setStyle(null)
    assertThat(stream.style).isEqualTo("original")
  }

  @Test
  fun `variation defaults to empty and can be set`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    assertThat(stream.variation).isEmpty()

    stream.setVariation("v1")
    assertThat(stream.variation).isEqualTo("v1")
  }

  @Test
  fun `setVariation with null does not change variation`() {
    val stream = ByteArrayResourceStream(byteArrayOf(), "text/plain")
    stream.setVariation("original")
    stream.setVariation(null)
    assertThat(stream.variation).isEqualTo("original")
  }
}
