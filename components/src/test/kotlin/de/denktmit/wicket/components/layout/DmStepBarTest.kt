package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmStepBarTest : WidgetTestBase() {

  private val steps = listOf(Step("Profile", TestHomePage::class.java), Step("Skills"), Step("Rates"), Step("Done"))

  @Test
  fun `finished, current and pending steps, the current announced, links only on finished steps with a page`() {
    val bar = render(DmStepBar("bar", steps, current = 2))
    assertHooks(bar)
    val items = tester.lastResponseAsString
    assertThat(items).contains("dm-step-done").contains("dm-step-current").contains("dm-step-pending")
    assertThat(Regex("aria-current=\"step\"").findAll(items).count()).isEqualTo(1)
    assertThat(tag("link", 0).name).isEqualTo("a")
    assertThat(tags("link").getOrNull(1)).isNull()
    assertThat(tag("label", 0).value).isEqualTo("Skills")
    assertThat(bar.stateOf(0)).isEqualTo("done")
    assertThat(bar.stateOf(2)).isEqualTo("current")
    assertThat(bar.stateOf(3)).isEqualTo("pending")
    assertStyleSheet("dm-step-bar")
  }

  @Test
  fun `a finished step without a page has no link`() {
    render(DmStepBar("bar", steps, current = 3))
    assertThat(tags("link")).hasSize(1)
    assertThat(tag("link", 0).name).isEqualTo("a")
    assertThat(tags("label").map { it.value }).containsExactly("Skills", "Rates", "Done")
  }
}
