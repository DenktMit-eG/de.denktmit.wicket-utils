package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.ByteArrayResourceStream
import de.denktmit.wicket.components.component.DmDownloadLink
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import java.util.Locale

class DmDownloadLinkTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val link = DmDownloadLink("d", fileName = "x.txt", outputData = byteArrayOf(1))

    assertThat(link.id).isEqualTo("d")
  }

  @Test
  fun `click schedules download with active request cycle`() {
    val link = DmDownloadLink("dl", fileName = "data.txt", outputData = "abc".toByteArray())

    assertThatCode { link.onClick() }.doesNotThrowAnyException()
  }

  @Test
  fun `byte array resource stream exposes byte length`() {
    val stream = ByteArrayResourceStream(byteArrayOf(1, 2, 3), "application/octet-stream")

    assertThat(stream.length().bytes()).isEqualTo(3)
  }

  @Test
  fun `byte array resource stream keeps locale style and variation defaults`() {
    val stream = ByteArrayResourceStream(byteArrayOf(1), "application/octet-stream")

    stream.setLocale(Locale.GERMAN)
    stream.setStyle("compact")
    stream.setVariation("v1")
    stream.setLocale(null)
    stream.setStyle(null)
    stream.setVariation(null)

    assertThat(stream.getLocale()).isEqualTo(Locale.GERMAN)
    assertThat(stream.getStyle()).isEqualTo("compact")
    assertThat(stream.getVariation()).isEqualTo("v1")
    assertThat(stream.getContentType()).isEqualTo("application/octet-stream")
    assertThatCode { stream.close() }.doesNotThrowAnyException()
  }

  @Test
  fun `constructs with explicit mime type`() {
    val link = DmDownloadLink("d", fileName = "x.bin", outputData = byteArrayOf(1, 2), mimeType = "application/pdf")

    assertThat(link.id).isEqualTo("d")
  }
}
