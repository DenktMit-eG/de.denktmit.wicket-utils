package de.denktmit.wicket.components.validator

import org.apache.wicket.validation.CompoundValidator
import org.apache.wicket.validation.validator.StringValidator

class MyStringValidator(
  min: Int,
  max: Int,
) : CompoundValidator<String>() {
  init {
    add(StringValidator.lengthBetween(min, max))
  }
}
