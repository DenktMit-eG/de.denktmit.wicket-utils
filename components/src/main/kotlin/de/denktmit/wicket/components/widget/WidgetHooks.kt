package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component

/**
 * The stable hooks every widget of the catalogue carries (requirement A.3): `dm-id-<wicketId>` names the instance,
 * `dm-<kind>` names the kind, derived from the nearest class in the hierarchy whose name starts with `Dm`
 * (`DmTabBox` becomes `dm-tab-box`, an anonymous subclass of it keeps `dm-tab-box`). Tests and style sheets select
 * by these two classes and never by the inner markup.
 */
object WidgetHooks {

  private val UPPERCASE = Regex("([A-Z])")

  /** The kind class of [component]: `dm-` plus the kebab-cased class name without its `Dm` prefix. */
  @JvmStatic
  fun kindCssClass(component: Component): String = "dm-" + kebab(kindName(component.javaClass))

  /** The instance class of [component]: `dm-id-` plus its Wicket id. */
  @JvmStatic
  fun idCssClass(component: Component): String = "dm-id-" + component.id

  /** Adds both hooks to [component]; idempotent in effect because a class set holds a name once. */
  @JvmStatic
  fun mark(component: Component) {
    component.addCssClass(idCssClass(component), kindCssClass(component))
  }

  private fun kindName(type: Class<*>): String {
    var current: Class<*>? = type
    while (current != null) {
      val name = current.simpleName
      if (name.startsWith("Dm") && name.length > 2) return name.removePrefix("Dm")
      current = current.superclass
    }
    return type.simpleName.ifEmpty { "widget" }
  }

  private fun kebab(name: String): String = name.replace(UPPERCASE) { "-" + it.value }.lowercase().removePrefix("-")
}

/** Adds the two stable hooks (`dm-id-<id>`, `dm-<kind>`) to this component; see [WidgetHooks]. */
fun Component.widgetHooks() {
  WidgetHooks.mark(this)
}
