package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAccordionTest : WidgetTestBase() {

  private fun tabs() = listOf(
    DmTabItem("General", icon = "settings") { TextPanel(it, "general body") },
    DmTabItem("Notifications", badge = "3") { TextPanel(it, "notification body") },
  )

  @Test
  fun `every section is a details element, the first open, exclusive through a shared name`() {
    val accordion = render(DmAccordion("acc", tabs()))
    assertHooks(accordion)
    assertThat(tags("items")).hasSize(2)
    assertThat(tag("items", 0).name).isEqualTo("details")
    assertThat(tag("items", 0).getAttribute("open")).isNotNull
    assertThat(tag("items", 1).getAttribute("open")).isNull()
    assertThat(tag("items", 0).getAttribute("name")).isEqualTo(accordion.markupId)
    assertThat(tag("items", 1).getAttribute("name")).isEqualTo(accordion.markupId)
    assertThat(html).contains("general body").contains("notification body")
    assertThat(tag("badge", 0).value).isEqualTo("3")
    assertStyleSheet("dm-accordion")
  }

  @Test
  fun `non-exclusive sections carry no name and may all start closed`() {
    render(DmAccordion("acc", tabs(), exclusive = false, open = -1))
    assertThat(tag("items", 0).getAttribute("name")).isNull()
    assertThat(tag("items", 0).getAttribute("open")).isNull()
  }
}
