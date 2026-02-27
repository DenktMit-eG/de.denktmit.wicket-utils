package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxCheckbox
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxCheckboxTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val checkbox = DmAjaxCheckbox("ac", Model.of(true))

    assertThat(checkbox.id).isEqualTo("ac")
  }
}
