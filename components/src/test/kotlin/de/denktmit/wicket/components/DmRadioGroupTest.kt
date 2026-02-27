package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmRadioGroup
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmRadioGroupTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val radio = DmRadioGroup<String>("r")

    assertThat(radio.id).isEqualTo("r")
  }
}
