package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class DmBorderLayoutTest : WidgetTestBase() {

  @Test
  fun `only placed regions render, collapsible ones are details with a title, resizable ones carry the class`() {
    val layout = DmBorderLayout("layout")
      .region(Region.TOP, TextPanel("top", "header"))
      .region(Region.START, TextPanel("start", "navigation"), Model.of("Navigation"), collapsible = true, resizable = true, open = false)
      .region(Region.CENTRE, TextPanel("centre", "content"))
    render(layout)
    assertHooks(layout)
    assertThat(tester.getTagByWicketId("endBox")).isNull()
    assertThat(tester.getTagByWicketId("bottomBox")).isNull()
    assertThat(tag("topBox").name).isEqualTo("div")
    assertThat(tag("startBox").name).isEqualTo("details")
    assertThat(tag("startBox").getAttribute("open")).isNull()
    assertThat(tag("startBox").getAttribute("class")).contains("dm-region-resizable")
    assertThat(tag("startTitle").value).isEqualTo("Navigation")
    assertThat(tester.getTagByWicketId("topTitle")).isNull()
    assertThat(html).contains("header").contains("navigation").contains("content")
    assertStyleSheet("dm-border-layout")
  }

  @Test
  fun `a region component must carry the region's id`() {
    assertThatThrownBy { DmBorderLayout("l").region(Region.END, TextPanel("start", "x")) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
