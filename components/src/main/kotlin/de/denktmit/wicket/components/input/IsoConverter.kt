package de.denktmit.wicket.components.input

import org.apache.wicket.util.convert.ConversionException
import org.apache.wicket.util.convert.IConverter
import java.time.DateTimeException
import java.util.Locale

/**
 * Converts between a temporal value and the ISO 8601 text the native date and time inputs submit, whatever the
 * user's locale: the browser shows the user's format and submits ISO (requirement E.41).
 */
class IsoConverter<T>(
  private val type: Class<T>,
  @Transient
  private val parse: (String) -> T,
  @Transient
  private val format: (T) -> String,
) : IConverter<T> {

  override fun convertToObject(value: String?, locale: Locale?): T? {
    if (value.isNullOrBlank()) return null
    return try {
      parse(value.trim())
    } catch (e: DateTimeException) {
      throw ConversionException(e).setResourceKey("IsoConverter.${type.simpleName}").setSourceValue(value).setTargetType(type).setLocale(locale)
    }
  }

  override fun convertToString(value: T?, locale: Locale?): String? = value?.let(format)
}
