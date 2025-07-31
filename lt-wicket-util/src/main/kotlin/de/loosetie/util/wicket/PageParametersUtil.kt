package de.loosetie.util.wicket

import org.apache.wicket.request.mapper.parameter.PageParameters
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

operator fun <T : String?> PageParameters.getValue(
  thisRef: Any?,
  property: KProperty<*>,
): T = get(property.name).toOptionalString() as T

operator fun <T : String?> PageParameters.setValue(
  thisRef: Any?,
  property: KProperty<*>,
  value: T,
) {
  set(property.name, value)
}

fun <T : String?> PageParameters.set(
  property: KProperty<T>,
  value: T,
) {
  set(property.name, value)
}

fun PageParameters.add(property: KProperty0<out String?>) {
  add(property.name, property.get())
}
