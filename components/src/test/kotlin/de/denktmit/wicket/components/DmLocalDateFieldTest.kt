package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmLocalDateField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DmLocalDateFieldTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val field = DmLocalDateField("ld", Model.of(LocalDate.now()))

    assertThat(field.id).isEqualTo("ld")
  }
}
