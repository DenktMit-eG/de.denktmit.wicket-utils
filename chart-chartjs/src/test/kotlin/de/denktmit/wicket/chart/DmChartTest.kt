package de.denktmit.wicket.chart

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmChartTest : WidgetTestBase() {

  private fun model() = ChartModel(ChartType.AREA, listOf("Jan", "Feb"), listOf(Series("Rate", listOf(100.0, 120.0), "#0b5f8a"), Series("Hours", listOf(8.0, 9.5))), "Month", "Value")

  @Test
  fun `renders the model as data for the script, the data as a table, and ships chart js from the webjar`() {
    val chart = render(DmChart("c", Model.of(model()), "16rem", Model.of("Rates by month")))
    assertHooks(chart)
    assertThat(tag("c").getAttribute("data-dm-chart-model")).contains("\"type\":\"area\"").contains("\"labels\":[\"Jan\",\"Feb\"]").contains("\"name\":\"Hours\"").contains("\"xTitle\":\"Month\"")
    assertThat(tag("c").getAttribute("data-dm-chart-call")).contains("Wicket.Ajax")
    assertThat(tag("c").getAttribute("style")).contains("--chart-height:16rem")
    assertThat(tag("caption").value).isEqualTo("Rates by month")
    assertThat(tags("head").map { it.value }).containsExactly("Jan", "Feb")
    assertThat(tags("name").map { it.value }).containsExactly("Rate", "Hours")
    assertThat(tags("cell").map { it.value }).containsExactly("100.0", "120.0", "8.0", "9.5")
    assertThat(html).containsPattern("chart\\.js/4\\.6\\.1/dist/chart\\.umd(-ver-[0-9]+)?\\.js").containsPattern("dm-chart-chartjs(-ver-[0-9]+)?\\.js")
    tester.startResourceReference(DmChart.CHARTJS)
    assertThat(tester.lastResponse.status).isEqualTo(200)
    assertThat(tester.lastResponseAsString).contains("Chart")
  }

  @Test
  fun `a click on a point reaches the page`() {
    var selected: Pair<Int, Int>? = null
    val chart = render(DmChart("c", Model.of(model())).apply { onSelect = { s, p, _ -> selected = s to p } })
    tester.request.setParameter("series", "1")
    tester.request.setParameter("point", "0")
    tester.executeBehavior(chart.behaviors.filterIsInstance<AbstractDefaultAjaxBehavior>().single())
    assertThat(selected).isEqualTo(1 to 0)
  }
}
