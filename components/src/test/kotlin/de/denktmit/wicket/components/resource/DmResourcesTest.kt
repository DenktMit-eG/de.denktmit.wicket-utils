package de.denktmit.wicket.components.resource

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmResourcesTest : WidgetTestBase() {

  @Test
  fun `the shared resources are served`() {
    for (reference in listOf(DmResources.BASE_CSS, DmResources.CORE_JS, DmResources.ICONS)) {
      tester.startResourceReference(reference)
      assertThat(tester.lastResponse.status).isEqualTo(200)
      assertThat(tester.lastResponseAsString).isNotBlank()
    }
  }

  @Test
  fun `the base sheet holds the tokens and every primitive`() {
    tester.startResourceReference(DmResources.BASE_CSS)
    val css = tester.lastResponseAsString
    assertThat(css).contains("--s0:", "--measure:", "--color-ink:")
    for (primitive in listOf(".stack", ".box", ".center", ".cluster", ".with-sidebar", ".switcher", ".cover", ".grid", ".frame", ".reel", ".imposter", ".icon")) {
      assertThat(css).contains("$primitive ")
    }
  }

  @Test
  fun `a widget sheet is looked up by name`() {
    tester.startResourceReference(DmResources.css("dm-loading-indicator"))
    assertThat(tester.lastResponse.status).isEqualTo(200)
  }
}
