package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmResourceReference
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmResourceReferenceTest : WicketTestBase() {

  @Test
  fun `returns non null resource`() {
    val reference = DmResourceReference("rr")

    assertThat(reference.getResource()).isNotNull
  }
}
