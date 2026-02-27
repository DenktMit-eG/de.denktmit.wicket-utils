package de.denktmit.wicket.components

import de.denktmit.rechnomat.webui.components.ReturnablePageLink
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReturnablePageLinkTest : WicketTestBase() {

  @Test
  fun `adds return parameter`() {
    val link = ReturnablePageLink("rp", WrapperTestPage::class.java)

    assertThat(link.pageParameters.get("returnTo").toString()).isEqualTo("previous")
  }
}
