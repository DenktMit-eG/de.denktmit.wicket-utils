package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmGenericPanel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmGenericPanelTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val panel = DmGenericPanel<String>("g")

    assertThat(panel.id).isEqualTo("g")
  }

  @Test
  fun `visibility and ajax flags are configurable`() {
    val panel = DmGenericPanel<String>("g")
    panel.visible = { false }
    panel.enableAjax()

    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isFalse()
    assertThat(panel.outputMarkupId).isTrue()
    assertThat(panel.outputMarkupPlaceholderTag).isTrue()
  }
}
