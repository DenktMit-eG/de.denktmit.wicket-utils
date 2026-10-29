package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmSeparatorTest : WidgetTestBase() {

  @Test
  fun `a separator is an hr with its orientation announced`() {
    val separator = render(DmSeparator("s", Orientation.VERTICAL, Space.S2), "<hr wicket:id=\"s\"/>")
    assertHooks(separator)
    assertThat(tag("s").name).isEqualTo("hr")
    assertThat(tag("s").getAttribute("role")).isEqualTo("separator")
    assertThat(tag("s").getAttribute("aria-orientation")).isEqualTo("vertical")
    assertThat(tag("s").getAttribute("class")).contains("dm-separator-vertical")
    assertThat(tag("s").getAttribute("style")).contains("--size:var(--s2)")
    assertStyleSheet("dm-separator")
  }

  @Test
  fun `a space is hidden from assistive technology`() {
    val space = render(DmSpace("sp", size = Space.S3), "<span wicket:id=\"sp\"></span>")
    assertHooks(space)
    assertThat(tag("sp").getAttribute("aria-hidden")).isEqualTo("true")
    assertThat(tag("sp").getAttribute("class")).contains("dm-space-horizontal")
    assertThat(tag("sp").getAttribute("style")).contains("--size:var(--s3)")
  }
}
