package de.denktmit.wicket.model

import org.apache.wicket.Component
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0


@Suppress("UNCHECKED_CAST")
operator fun <T : String?> PageParameters.getValue(thisRef: Any?, property: KProperty<*>): T {
  return get(property.name).toOptionalString() as T
}

operator fun <T : String?> PageParameters.setValue(thisRef: Any?, property: KProperty<*>, value: T) {
  set(property.name, value)
}

fun <T : String?> PageParameters.set(property: KProperty<T>, value: T) {
  set(property.name, value)
}

fun PageParameters.add(property: KProperty0<String?>) {
  add(property.name, property.get())
}


interface PageParams : Serializable {
  val pp: PageParameters
}

fun <P : PageParams> Component.setResponsePage(
  pageClass: Class<out IRequestablePage>,
  params: P,
  block: (P.() -> Unit) = {}
) {
  setResponsePage(pageClass, params.apply(block).pp)
}
