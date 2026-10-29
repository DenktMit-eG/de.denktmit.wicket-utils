package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.WebMarkupContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmLoadingIndicatorTest : WidgetTestBase() {

  @Test
  fun `renders hidden with a status role and its sheet`() {
    val indicator = render(DmLoadingIndicator("busy"))
    assertHooks(indicator)
    assertThat(tag("busy").getAttribute("style")).contains("display:none")
    assertThat(tag("busy").getAttribute("role")).isEqualTo("status")
    assertThat(tag("busy").getAttribute("id")).isEqualTo(indicator.markupId)
    assertThat(html).contains("Loading")
    assertStyleSheet("dm-loading-indicator")
  }

  @Test
  fun `the behaviour names the indicator's markup id`() {
    val indicator = DmLoadingIndicator("busy")
    val region = WebMarkupContainer("region")
    indicator.indicates(region)
    render(indicator)
    val behavior = region.behaviors.filterIsInstance<LoadingIndicatorBehavior>().single()
    assertThat(behavior.ajaxIndicatorMarkupId).isEqualTo(indicator.markupId)
  }
}
