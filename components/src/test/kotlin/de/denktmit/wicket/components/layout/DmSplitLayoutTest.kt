package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class DmSplitLayoutTest : WidgetTestBase() {

  @Test
  fun `two panes with a divider, the first sized, collapse controls hidden until script shows them`() {
    val split = render(DmSplitLayout("split", Orientation.VERTICAL, TextPanel(DmSplitLayout.FIRST, "left"), TextPanel(DmSplitLayout.SECOND, "right"), "30%"))
    assertHooks(split)
    assertThat(tag("split").getAttribute("data-dm-split")).isEqualTo("vertical")
    assertThat(tag("split").getAttribute("style")).contains("--first-size:30%")
    assertThat(tag("divider").getAttribute("role")).isEqualTo("separator")
    assertThat(tag("divider").getAttribute("aria-orientation")).isEqualTo("horizontal")
    assertThat(tag("collapseFirst").getAttribute("hidden")).isNotNull
    assertThat(html).contains("left").contains("right")
    assertStyleSheet("dm-split-layout")
    assertScript("dm-split-layout")
  }

  @Test
  fun `no collapse controls when not collapsible`() {
    render(DmSplitLayout("split", first = TextPanel("first", "a"), second = TextPanel("second", "b"), collapsible = false))
    assertThat(tester.getTagByWicketId("collapseFirst")).isNull()
  }

  @Test
  fun `pane ids are checked`() {
    assertThatThrownBy { DmSplitLayout("split", first = TextPanel("x", "a"), second = TextPanel("second", "b")) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
