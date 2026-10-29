package de.denktmit.wicket.components.resource

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WebjarResourceReferenceTest : WidgetTestBase() {

  @Test
  fun `serves a file from a webjar on the classpath with its media type`() {
    tester.startResourceReference(WebjarResourceReference("sample/1.0/sample.js"))
    assertThat(tester.lastResponse.status).isEqualTo(200)
    assertThat(tester.lastResponse.contentType).contains("javascript")
    assertThat(tester.lastResponseAsString).contains("loaded")
  }
}
