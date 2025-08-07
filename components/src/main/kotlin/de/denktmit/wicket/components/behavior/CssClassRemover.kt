package de.denktmit.wicket.components.behavior

import de.denktmit.wicket.model.modelOf
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
