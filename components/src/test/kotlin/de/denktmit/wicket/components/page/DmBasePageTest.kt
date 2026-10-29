package de.denktmit.wicket.components.page

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.DefaultMarkupResourceStreamProvider
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class DmBasePageTest : WidgetTestBase() {

  class SamplePage : DmBasePage(), IMarkupResourceStreamProvider {
    override fun title(): IModel<String> = Model.of("Sample")

    override fun onInitialize() {
      super.onInitialize()
      +Label("content", "Body")
      info("Saved")
    }

    override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream =
      if (containerClass == SamplePage::class.java) {
        StringResourceStream("<wicket:extend><p wicket:id=\"content\"></p></wicket:extend>")
      } else {
        DefaultMarkupResourceStreamProvider().getMarkupResourceStream(container, containerClass)
      }
  }

  @Test
  fun `carries title, language, base sheet, feedback, loading indicator and the child region`() {
    tester.session.locale = Locale.GERMANY
    tester.startPage(SamplePage::class.java)
    tester.assertRenderedPage(SamplePage::class.java)
    assertThat(tag("title").value).isEqualTo("Sample")
    assertThat(tag("html").getAttribute("lang")).isEqualTo("de-DE")
    assertStyleSheet("dm-base")
    assertScript("dm-core")
    assertThat(html).contains("Body").contains("Saved")
    assertThat(tag("feedback").getAttribute("class")).contains("dm-feedback")
    assertThat(tag("loading").getAttribute("class")).contains("dm-loading-indicator-page")
    assertThat(tag("notifications").getAttribute("class")).contains("dm-notifications")
    tester.assertNoErrorMessage()
  }
}
