package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAnchorLayoutTest : WidgetTestBase() {

  @Test
  fun `children carry their anchor class and the container its height`() {
    val layout = DmAnchorLayout("l", height = "12rem", inset = Space.S1)
    layout.add(layout.anchor(Label("a", "A"), Anchor.TOP_END), layout.anchor(Label("b", "B"), Anchor.CENTER))
    render(layout, "<div wicket:id=\"l\"><span wicket:id=\"a\"></span><span wicket:id=\"b\"></span></div>")
    assertHooks(layout)
    assertThat(tag("l").getAttribute("style")).contains("--height:12rem", "--inset:var(--s1)")
    assertThat(tag("a").getAttribute("class")).contains("dm-anchored", "dm-anchor-top-end")
    assertThat(tag("b").getAttribute("class")).contains("dm-anchor-center")
    assertStyleSheet("dm-anchor-layout")
  }
}
