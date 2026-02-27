package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmPanel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPanelTest : WicketTestBase() {

  @Test
  fun `visibility and ajax flags are configurable`() {
    val panel = object : DmPanel("p") {}
    panel.visible = { false }
    panel.enableAjax()

    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isFalse()
    assertThat(panel.outputMarkupId).isTrue()
    assertThat(panel.outputMarkupPlaceholderTag).isTrue()
  }
}
