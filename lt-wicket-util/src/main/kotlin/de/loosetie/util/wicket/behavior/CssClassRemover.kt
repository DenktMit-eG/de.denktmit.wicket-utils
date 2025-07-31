package de.loosetie.util.wicket.behavior

import de.loosetie.util.wicket.modelOf
import org.apache.wicket.AttributeModifier
import java.util.regex.Pattern

class CssClassRemover(
  cssClass: String,
) : AttributeModifier("class", modelOf { cssClass }) {
  override fun newValue(
    currentValue: String?,
    valueToRemove: String?,
  ): String? {
    if (currentValue == null) {
      return ""
    }
    val patternString = "(^|\\s+)" + Pattern.quote(valueToRemove).toString() + "(?!\\S)"
    return Pattern.compile(patternString, Pattern.CASE_INSENSITIVE).matcher(currentValue).replaceAll("")
  }
}
