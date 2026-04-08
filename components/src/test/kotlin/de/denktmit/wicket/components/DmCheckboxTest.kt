package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmCheckbox
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmCheckboxTest : WicketTestBase() {

  private class Holder(var checked: Boolean = true)

  @Test
  fun `constructs with id`() {
    val checkbox = DmCheckbox("cb", Model.of(true))

    assertThat(checkbox.id).isEqualTo("cb")
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val checkbox = DmCheckbox("cb", Model.of(true))
    invokeDeclared(checkbox, "onInitialize")

    assertThat(checkbox.behaviors).isNotEmpty
  }

  @Test
  fun `secondary KProperty constructor`() {
    val holder = Holder()
    val checkbox = DmCheckbox(holder::checked)

    assertThat(checkbox.id).isEqualTo("checked")
  }
}
