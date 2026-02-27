package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmCheckbox
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmCheckboxTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val checkbox = DmCheckbox("cb", Model.of(true))

    assertThat(checkbox.id).isEqualTo("cb")
  }
}
