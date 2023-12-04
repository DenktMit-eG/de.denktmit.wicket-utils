package de.denktmit.wicket.components

import org.apache.wicket.ClassAttributeModifier
import org.apache.wicket.Component

fun Component.addCssClass(vararg name: String) {
  add(object : ClassAttributeModifier() {
    override fun update(set: MutableSet<String>): Set<String> {
      set.addAll(name)
      return set
    }
  })
}

fun Component.removeCssClass(vararg name: String) {
  add(object : ClassAttributeModifier() {
    override fun update(set: MutableSet<String>): Set<String> {
      set.removeAll(name)
      return set
    }
  })
}

private val UPPERCASE_CHAR_REGEX by lazy { Regex("([A-Z])") }
fun Any.classNameAsCssClass(): String {
  return javaClass.simpleName.replace(UPPERCASE_CHAR_REGEX) { "-" + it.value }.lowercase()
}
