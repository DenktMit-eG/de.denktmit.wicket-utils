package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.apache.wicket.request.resource.ResourceReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmWidgetPanelTest : WidgetTestBase() {

  private class DmSample(id: String, init: DmWidgetPanel.() -> Unit = {}) : DmWidgetPanel(id, null, init), IMarkupResourceStreamProvider {
    override fun addComponents() {
      +Label("text", "hello")
    }

    override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream =
      StringResourceStream("<wicket:panel><span wicket:id=\"text\"></span></wicket:panel>")

    override fun styleSheet(): ResourceReference = DmResources.css("dm-loading-indicator")

    override fun script(): ResourceReference = DmResources.CORE_JS
  }

  @Test
  fun `renders with hooks, sheet and script`() {
    val widget = render(DmSample("sample"))
    assertHooks(widget)
    assertThat(tag("sample").getAttribute("class")).contains("dm-sample")
    assertStyleSheet("dm-base")
    assertStyleSheet("dm-loading-indicator")
    assertScript("dm-core")
    assertThat(html).contains("hello")
  }

  @Test
  fun `init lambda runs after the widget's own children exist`() {
    var seen = false
    render(DmSample("sample") { seen = get("text") != null })
    assertThat(seen).isTrue()
  }

  @Test
  fun `visible and enabled rules apply on render`() {
    val widget = DmSample("sample")
    widget.visible = { false }
    widget.enabled = { false }
    render(widget)
    assertThat(widget.isVisible).isFalse()
    assertThat(widget.isEnabled).isFalse()
    assertThat(tester.getTagByWicketId("sample")).isNull()
  }

  @Test
  fun `cssClass by rule`() {
    render(DmSample("sample") { cssClass("busy") { true } })
    assertThat(tag("sample").getAttribute("class")).contains("busy")
  }
}
