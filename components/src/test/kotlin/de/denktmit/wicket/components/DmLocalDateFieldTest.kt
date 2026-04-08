package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmLocalDateField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DmLocalDateFieldTest : WicketTestBase() {

  private class Holder(var date: LocalDate? = LocalDate.now())

  @Test
  fun `constructs with id`() {
    val field = DmLocalDateField("ld", Model.of(LocalDate.now()))

    assertThat(field.id).isEqualTo("ld")
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val field = DmLocalDateField("ld", Model.of(LocalDate.now()))
    invokeDeclared(field, "onInitialize")

    assertThat(field.behaviors).isNotEmpty
  }

  @Test
  fun `secondary KProperty constructor`() {
    val holder = Holder()
    val field = DmLocalDateField(holder::date)

    assertThat(field.id).isEqualTo("date")
  }
}
