package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmDocumentViewerTest : WidgetTestBase() {

  @Test
  fun `shows the document in the browser's viewer with open and download links`() {
    val viewer = render(DmDocumentViewer("doc", "/files/profile.pdf", "Profile of Anna", height = "30rem", fileName = "anna.pdf"))
    assertHooks(viewer)
    assertThat(tag("document").name).isEqualTo("object")
    assertThat(tag("document").getAttribute("data")).isEqualTo("/files/profile.pdf")
    assertThat(tag("document").getAttribute("type")).isEqualTo("application/pdf")
    assertThat(tag("document").getAttribute("aria-label")).isEqualTo("Profile of Anna")
    assertThat(tag("document").getAttribute("style")).contains("height:30rem")
    assertThat(tag("fallback").value).isEqualTo("Profile of Anna")
    assertThat(tag("open").getAttribute("href")).isEqualTo("/files/profile.pdf")
    assertThat(tag("open").getAttribute("target")).isEqualTo("_blank")
    assertThat(tag("download").getAttribute("download")).isEqualTo("anna.pdf")
    assertStyleSheet("dm-display")
  }
}
