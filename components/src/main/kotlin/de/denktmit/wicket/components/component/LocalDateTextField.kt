package de.denktmit.wicket.components.component

import org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.util.convert.converter.LocalDateConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class LocalDateTextField(
  id: String,
  model: IModel<LocalDate>,
) : TextField<LocalDate>(id, model),
  ITextFormatProvider {
  override fun createConverter(type: Class<*>?): IConverter<LocalDate>? {
    val converter =
      object : LocalDateConverter() {
        override fun getDateTimeFormatter(locale: Locale?): DateTimeFormatter =
          DateTimeFormatter.ofPattern(textFormat, locale ?: Locale.getDefault())
      }

    if (LocalDate::class.java.isAssignableFrom(type)) {
      return converter
    }
    return null
  }

  override fun getInputTypes(): Array<String> = arrayOf("date")

  override fun getTextFormat(): String = "yyyy-MM-dd"
}
