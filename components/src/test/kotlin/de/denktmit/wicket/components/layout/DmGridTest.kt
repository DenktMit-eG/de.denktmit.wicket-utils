package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class DmGridTest : WidgetTestBase() {

  @Test
  fun `grid carries the minimum column width and spans`() {
    val grid = DmGrid("grid", minColumn = "12rem", space = Space.S2)
    grid.add(grid.span(Label("wide", "W"), 2), Label("narrow", "N"))
    render(grid, "<div wicket:id=\"grid\"><span wicket:id=\"wide\"></span><span wicket:id=\"narrow\"></span></div>")
    assertHooks(grid)
    assertThat(tag("grid").getAttribute("class")).contains("grid")
    assertThat(tag("grid").getAttribute("style")).contains("--min-column:12rem", "--space:var(--s2)")
    assertThat(tag("wide").getAttribute("class")).contains("dm-span-2")
    assertStyleSheet("dm-grid")
  }

  @Test
  fun `a span outside 1 to 6 is refused`() {
    assertThatThrownBy { DmGrid("grid").span(Label("x", "x"), 7) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
