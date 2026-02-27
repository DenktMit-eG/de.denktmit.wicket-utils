package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmBorder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmBorderTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val border = DmBorder("b")

    assertThat(border.id).isEqualTo("b")
  }

  @Test
  fun `onConfigure applies visibility and ajax flags`() {
    val border = DmBorder("b")
    border.visible = { false }
    border.enableAjax()

    invokeDeclared(border, "onConfigure")

    assertThat(border.isVisible).isFalse()
    assertThat(border.outputMarkupId).isTrue()
    assertThat(border.outputMarkupPlaceholderTag).isTrue()
  }
}
