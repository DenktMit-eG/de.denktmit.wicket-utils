package de.denktmit.wicket.components

import de.denktmit.wicket.components.resource.ClassPathResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ClassPathResourceTest : WicketTestBase() {

  @Test
  fun `stores classpath path`() {
    val resource = ClassPathResource("missing.file")

    assertThat(resource.path).isEqualTo("missing.file")
  }
}
