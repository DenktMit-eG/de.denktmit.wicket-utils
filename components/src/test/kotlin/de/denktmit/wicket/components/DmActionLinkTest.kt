package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmActionLink
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmActionLinkTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val link = DmActionLink("a")

    assertThat(link.id).isEqualTo("a")
  }

  @Test
  fun `invokes click callback`() {
    var clicked = false
    val link = DmActionLink("x")
    link.onClick = { clicked = true }

    link.onClick()

    assertThat(clicked).isTrue()
  }
}
