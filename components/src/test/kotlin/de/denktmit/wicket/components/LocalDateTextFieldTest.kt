package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.LocalDateTextField
import org.apache.wicket.model.Model
import org.apache.wicket.util.convert.IConverter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Locale

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

  @Suppress("UNCHECKED_CAST")
  @Test
  fun `converter formats and parses dates`() {
    val field = LocalDateTextField("ldt", Model.of(LocalDate.of(2024, 3, 15)))

    val converter = invokeDeclared(field, "createConverter", LocalDate::class.java) as IConverter<LocalDate>
    val formatted = converter.convertToString(LocalDate.of(2024, 3, 15), Locale.US)
    val parsed = converter.convertToObject("2024-03-15", Locale.US)

    assertThat(formatted).isEqualTo("2024-03-15")
    assertThat(parsed).isEqualTo(LocalDate.of(2024, 3, 15))
  }
}
