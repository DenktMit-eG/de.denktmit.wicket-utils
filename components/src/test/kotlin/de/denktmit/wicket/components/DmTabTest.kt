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
}
