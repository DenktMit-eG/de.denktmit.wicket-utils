package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.LazyPageLink
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LazyPageLinkTest : WicketTestBase() {

  @Test
  fun `constructs with lazy page provider`() {
    val link = LazyPageLink("lazy", pageClass = { WrapperTestPage::class.java }, parameters = { PageParameters() })

    assertThat(link.id).isEqualTo("lazy")
  }
}
