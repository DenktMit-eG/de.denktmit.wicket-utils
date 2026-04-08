package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmTab
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTabTest : WicketTestBase() {

  @Test
  fun `returns configured panel`() {
    val tab = DmTab(Model.of("Tab"), WebMarkupContainer("content"), "icon")

    assertThat(tab.getPanel("panel")).isEqualTo(tab.markupContainer)
  }

  @Test
  fun `icon badge and tooltip accessors return configured values`() {
    val tab = DmTab(
      Model.of("Tab"),
      WebMarkupContainer("content"),
      cssMobileIcon = "icon-home",
      badge = Model.of("3"),
      tooltip = { "Tooltip text" },
    )

    assertThat(tab.cssMobileIcon).isEqualTo("icon-home")
    assertThat(tab.badge?.`object`).isEqualTo("3")
    assertThat(tab.tooltip?.invoke()).isEqualTo("Tooltip text")
    assertThat(tab.title.`object`).isEqualTo("Tab")
  }

  @Test
  fun `tabCmp can be assigned and retrieved`() {
    val tab = DmTab(Model.of("Tab"), WebMarkupContainer("content"), "icon")
    val container = WebMarkupContainer("tab")

    tab.tabCmp = container

    assertThat(tab.tabCmp).isEqualTo(container)
  }
}
