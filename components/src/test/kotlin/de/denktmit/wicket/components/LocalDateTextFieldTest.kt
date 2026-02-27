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

  @Test
  fun `converter and input type branches are covered`() {
    val field = LocalDateTextField("ldt", Model.of(LocalDate.now()))

    val converter = invokeDeclared(field, "createConverter", LocalDate::class.java)
    val wrongTypeConverter = invokeDeclared(field, "createConverter", String::class.java)

    assertThat(converter).isNotNull
    assertThat(wrongTypeConverter).isNull()
    assertThat(invokeDeclared(field, "getInputTypes") as Array<*>).containsExactly("date")
  }
}
