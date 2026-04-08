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

  @Test
  fun `pageClass lambda returns expected class`() {
    val link = LazyPageLink("lazy", pageClass = { WrapperTestPage::class.java }, parameters = { PageParameters() })

    assertThat(link.pageClass()).isEqualTo(WrapperTestPage::class.java)
  }

  @Test
  fun `parameters lambda returns PageParameters`() {
    val params = PageParameters().add("key", "value")
    val link = LazyPageLink("lazy", pageClass = { WrapperTestPage::class.java }, parameters = { params })

    assertThat(link.parameters()).isEqualTo(params)
    assertThat(link.parameters().get("key").toString()).isEqualTo("value")
  }
}
