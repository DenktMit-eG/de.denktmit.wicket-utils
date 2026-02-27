package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.ByteArrayResourceStream
import de.denktmit.wicket.components.component.DmDownloadLink
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

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
}
