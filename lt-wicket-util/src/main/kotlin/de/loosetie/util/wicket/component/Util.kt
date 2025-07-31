package de.loosetie.util.wicket.component

import org.apache.wicket.ClassAttributeModifier
import org.apache.wicket.Component

fun Component.addCssClass(vararg name: String) {
  add(
    object : ClassAttributeModifier() {
      override fun update(set: MutableSet<String>): Set<String> {
        set.addAll(name)
        return set
      }
    },
  )
}
