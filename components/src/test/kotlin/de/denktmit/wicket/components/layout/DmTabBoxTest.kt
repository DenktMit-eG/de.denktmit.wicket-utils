package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTabBoxTest : WidgetTestBase() {

  private fun tabs() = listOf(
    DmTabItem("Profile", icon = "user") { TextPanel(it, "profile body") },
    DmTabItem("Skills", badge = "12", closable = true) { TextPanel(it, "skills body") },
    DmTabItem("Rates") { TextPanel(it, "rates body") },
  )

  @Test
  fun `renders a tablist with icon, badge and the selected panel, switches by link`() {
    val box = render(DmTabBox("tabs", tabs(), Side.START))
    assertHooks(box)
    assertThat(tag("tabs").getAttribute("class")).contains("dm-tab-box-start")
    assertThat(tag("tabs-container").getAttribute("role")).isEqualTo("tablist")
    assertThat(tag("tabs-container").getAttribute("aria-orientation")).isEqualTo("vertical")
    assertThat(tags("link")).hasSize(3)
    assertThat(tag("link", 0).getAttribute("aria-selected")).isEqualTo("true")
    assertThat(tag("link", 1).getAttribute("aria-selected")).isEqualTo("false")
    assertThat(tags("icon").map { it.getAttribute("class") }.filter { it.contains("dm-icon") }).isNotEmpty
    assertThat(tag("badge").value).isEqualTo("12")
    assertThat(tags("close")).hasSize(1)
    assertThat(html).contains("profile body").doesNotContain("skills body")
    assertStyleSheet("dm-tab-box")

    tester.clickLink("tabs:tabs-container:tabs:1:link")
    assertThat(html).contains("skills body").doesNotContain("profile body")
    assertThat(box.selectedTab).isEqualTo(1)
  }

  @Test
  fun `ajax tabs switch by ajax and fall back to the link`() {
    val box = render(DmTabBox("tabs", tabs(), ajax = true))
    tester.clickLink("tabs:tabs-container:tabs:2:link", true)
    assertThat(box.selectedTab).isEqualTo(2)
    assertThat(html).contains("rates body")
  }

  @Test
  fun `closing a tab removes it and reports it`() {
    var closed: DmTabItem? = null
    val box = DmTabBox("tabs", tabs()).apply { onClose = { closed = it }; setSelectedTab(1) }
    render(box)
    tester.clickLink("tabs:tabs-container:tabs:1:close")
    assertThat(closed?.title?.`object`).isEqualTo("Skills")
    assertThat(box.tabs).hasSize(2)
    assertThat(tags("link")).hasSize(2)
    assertThat(box.selectedTab).isEqualTo(1)
    assertThat(html).contains("rates body")
  }
}
