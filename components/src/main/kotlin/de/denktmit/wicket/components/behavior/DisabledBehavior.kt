package de.denktmit.wicket.components.behavior

import org.apache.wicket.AttributeModifier
import org.apache.wicket.ClassAttributeModifier
import org.apache.wicket.Component
import java.io.Serializable

class DisabledBehavior(
  @Transient
  private val disabledIf: () -> Boolean = { true },
) : AttributeModifier("disabled", "disabled") {
  override fun newValue(
    currentValue: String?,
    replacementValue: String,
  ): Serializable =
    if (disabledIf()) {
      replacementValue
    } else {
      VALUELESS_ATTRIBUTE_REMOVE
    }
}

fun Component.disabled(disabledIf: () -> Boolean = { true }) {
  add(DisabledBehavior(disabledIf))
  add(
    object : ClassAttributeModifier() {
      override fun update(set: MutableSet<String>): Set<String> {
        if (disabledIf()) {
          set.add("disabled")
        }
        return set
      }
    },
  )
}
