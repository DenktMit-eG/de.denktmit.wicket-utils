package de.denktmit.wicket.components

import org.apache.wicket.Page
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPageTest : WicketTestBase() {

  @Test
  fun `is a wicket page`() {
    val page = WrapperTestPage(PageParameters())

    assertThat(page).isInstanceOf(Page::class.java)
  }
}
