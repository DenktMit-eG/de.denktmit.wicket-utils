package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.LocalDateTextField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateTextFieldTest : WicketTestBase() {

  @Test
  fun `uses html date format`() {
    val field = LocalDateTextField("ldt", Model.of(LocalDate.now()))

    assertThat(field.textFormat).isEqualTo("yyyy-MM-dd")
  }
}
