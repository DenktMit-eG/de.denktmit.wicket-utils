package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.base.unaryPlus
import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmBoxTest : WidgetTestBase() {

  private val body = "<div wicket:id=\"box\"><span wicket:id=\"a\"></span><span wicket:id=\"b\"></span></div>"

  @Test
  fun `a horizontal box is a cluster with gap, alignment and wrapping as custom properties`() {
    val box = render(DmHBox("box", Space.S1, Align.END, Justify.BETWEEN, wrap = false) { +Label("a", "A"); +Label("b", "B") }, body)
    assertHooks(box)
    assertThat(tag("box").getAttribute("class")).contains("cluster", "dm-h-box")
    assertThat(tag("box").getAttribute("style")).contains("--space:var(--s1)", "--align:flex-end", "--justify:space-between", "--wrap:nowrap")
    assertStyleSheet("dm-box")
  }

  @Test
  fun `a vertical box is a stack and a child may grow`() {
    val box = DmVBox("box", Space.NONE)
    val b = Label("b", "B")
    box.add(Label("a", "A"), box.grow(b))
    render(box, body)
    assertThat(tag("box").getAttribute("class")).contains("stack", "dm-v-box")
    assertThat(tag("box").getAttribute("style")).contains("--space:0")
    assertThat(tag("b").getAttribute("class")).contains("dm-grow")
  }
}
