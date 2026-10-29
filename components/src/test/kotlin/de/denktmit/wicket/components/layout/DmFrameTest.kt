package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmFrameTest : WidgetTestBase() {

  @Test
  fun `renders an iframe with source, title, size, lazy loading and sandbox`() {
    val frame = render(DmFrame("f", "https://example.org/doc", "The document", "50%", "20rem"), "<iframe wicket:id=\"f\"></iframe>")
    assertHooks(frame)
    val tag = tag("f")
    assertThat(tag.getAttribute("src")).isEqualTo("https://example.org/doc")
    assertThat(tag.getAttribute("title")).isEqualTo("The document")
    assertThat(tag.getAttribute("loading")).isEqualTo("lazy")
    assertThat(tag.getAttribute("style")).contains("width:50%", "height:20rem")
    assertThat(tag.getAttribute("sandbox")).isEqualTo("allow-scripts allow-same-origin")
    assertStyleSheet("dm-frame")
  }

  @Test
  fun `no sandbox when the author lifts it`() {
    render(DmFrame("f", "https://example.org", "x").apply { sandbox = null }, "<iframe wicket:id=\"f\"></iframe>")
    assertThat(tag("f").getAttribute("sandbox")).isNull()
  }
}
