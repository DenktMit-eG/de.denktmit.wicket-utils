package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxLink
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxLinkTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val link = DmAjaxLink<Any>("al", click = {})

    assertThat(link.id).isEqualTo("al")
  }

  @Test
  fun `click handler and visible callback are used`() {
    var clicked = false
    val link =
      DmAjaxLink<Any>("al", click = {
        clicked = true
      })
    link.visible = { false }

    link.onClick(mockk<AjaxRequestTarget>(relaxed = true))
    invokeDeclared(link, "onConfigure")

    assertThat(clicked).isTrue()
    assertThat(link.isVisible).isFalse()
  }
}
