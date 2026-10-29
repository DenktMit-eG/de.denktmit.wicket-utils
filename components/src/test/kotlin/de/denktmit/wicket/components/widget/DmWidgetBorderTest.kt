package de.denktmit.wicket.components.widget

import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmWidgetBorderTest : WidgetTestBase() {

  private class DmFrameBorder(id: String, init: DmWidgetBorder.() -> Unit = {}) : DmWidgetBorder(id, null, init), IMarkupResourceStreamProvider {
    override fun addComponents() {
      addToBorder(Label("caption", "Caption"))
    }

    override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream =
      StringResourceStream("<wicket:border><p wicket:id=\"caption\"></p><div class=\"body\"><wicket:body/></div></wicket:border>")
  }

  @Test
  fun `wraps the author's markup and renders its own parts`() {
    val border = render(DmFrameBorder("frame") { +Label("inner", "inside") }, "<div wicket:id=\"frame\"><span wicket:id=\"inner\"></span></div>")
    assertHooks(border)
    assertThat(html).contains("Caption").contains("inside")
    assertThat(html.indexOf("Caption")).isLessThan(html.indexOf("inside"))
  }
}
