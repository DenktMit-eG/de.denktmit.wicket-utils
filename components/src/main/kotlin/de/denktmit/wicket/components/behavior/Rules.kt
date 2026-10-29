package de.denktmit.wicket.components.behavior

import org.apache.wicket.ClassAttributeModifier
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import java.util.function.BooleanSupplier

/**
 * Visibility by rule (requirement I.89): the rule runs on every render, in the component's configure phase, so the
 * page author never calls `setVisible` by hand. Attach with [visibleIf].
 */
class VisibleRule(
  @Transient
  private val rule: BooleanSupplier,
) : Behavior() {
  override fun onConfigure(component: Component) {
    component.isVisible = rule.asBoolean
  }
}

/** Enabled state by rule (requirement I.89), evaluated on every render. Attach with [enabledIf]. */
class EnabledRule(
  @Transient
  private val rule: BooleanSupplier,
) : Behavior() {
  override fun onConfigure(component: Component) {
    component.isEnabled = rule.asBoolean
  }
}

/** A CSS class added while the rule holds and removed otherwise (requirement I.90). Attach with [cssClassIf]. */
class CssClassRule(
  private val cssClass: String,
  @Transient
  private val rule: BooleanSupplier,
) : ClassAttributeModifier() {
  override fun update(set: MutableSet<String>): Set<String> {
    if (rule.asBoolean) set.add(cssClass) else set.remove(cssClass)
    return set
  }
}

/** Makes this component visible exactly when [rule] holds, evaluated on every render. */
fun <C : Component> C.visibleIf(rule: () -> Boolean): C = apply { add(VisibleRule(rule)) }

/** Enables this component exactly when [rule] holds, evaluated on every render. */
fun <C : Component> C.enabledIf(rule: () -> Boolean): C = apply { add(EnabledRule(rule)) }

/** Marks this component read-only exactly when [rule] holds, evaluated on every render. */
fun <C : Component> C.readOnlyIf(rule: () -> Boolean): C = apply { add(ReadonlyBehavior(rule)) }

/** Adds [cssClass] to this component exactly when [rule] holds, evaluated on every render. */
fun <C : Component> C.cssClassIf(cssClass: String, rule: () -> Boolean): C = apply { add(CssClassRule(cssClass, rule)) }
