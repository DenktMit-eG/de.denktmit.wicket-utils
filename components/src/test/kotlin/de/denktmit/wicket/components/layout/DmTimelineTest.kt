package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTimelineTest : WidgetTestBase() {

  private val events = listOf(
    TimelineEvent("Joined", "March 2024", "First day", "2024-03-01"),
    TimelineEvent("Promoted", "June 2025", "Lead"),
  )

  @Test
  fun `events render in order with time, title and body`() {
    val timeline = render(DmTimeline("tl", events))
    assertHooks(timeline)
    assertThat(tag("time", 0).name).isEqualTo("time")
    assertThat(tag("time", 0).getAttribute("datetime")).isEqualTo("2024-03-01")
    assertThat(tag("time", 1).getAttribute("datetime")).isNull()
    assertThat(tag("title", 0).value).isEqualTo("Joined")
    assertThat(tag("body", 1).value).contains("Lead")
    assertThat(html.indexOf("Joined")).isLessThan(html.indexOf("Promoted"))
    assertStyleSheet("dm-timeline")
  }

  @Test
  fun `the body may be a component per event`() {
    render(DmTimeline("tl", events).body { Label(DmTimeline.BODY, "custom " + it.title) })
    assertThat(tag("body", 0).value).isEqualTo("custom Joined")
  }
}
