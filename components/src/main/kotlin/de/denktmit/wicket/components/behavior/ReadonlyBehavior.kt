package de.denktmit.wicket.components.behavior

import org.apache.wicket.AttributeModifier
import java.io.Serializable

class ReadonlyBehavior(
  @Transient
  private val readonlyIf: () -> Boolean = { true },
) : AttributeModifier("readonly", "true") {
  override fun newValue(
    currentValue: String?,
    replacementValue: String,
  ): Serializable =
    if (readonlyIf()) {
      replacementValue
    } else {
      VALUELESS_ATTRIBUTE_REMOVE
    }
}
