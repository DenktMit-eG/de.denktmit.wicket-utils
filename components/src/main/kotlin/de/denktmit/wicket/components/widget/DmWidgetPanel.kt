package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.behavior.CssClassRule
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * Base of every panel-shaped widget of the catalogue: the stable hooks (A.3), `visible` and `enabled` by rule (I.89),
 * `cssClass` by rule (I.90), the widget's own style sheet and script contributed once per page (A.7, K.105), the
 * `+child` operator and the `init` lambda in which the page author configures the widget.
 *
 * A subclass names its style sheet through [styleSheet] and its script through [script]; both default to none. The
 * base sheet with tokens and the layout primitives is contributed by every widget, so a page is styled even when it
 * does not extend `DmBasePage`.
 */
abstract class DmWidgetPanel
@JvmOverloads
constructor(
  id: String,
  model: IModel<*>? = null,
  @Transient
  private val init: DmWidgetPanel.() -> Unit = {},
) : Panel(id, model) {

  /** Visibility rule, evaluated on every render; `null` leaves the Wicket default. */
  @Transient
  var visible: (() -> Boolean)? = null

  /** Enabled rule, evaluated on every render; `null` leaves the Wicket default. */
  @Transient
  var enabled: (() -> Boolean)? = null

  /** Adds a child to this widget. */
  operator fun Component.unaryPlus() {
    this@DmWidgetPanel.add(this)
  }

  /** Adds a behaviour to this widget. */
  operator fun Behavior.unaryPlus() {
    this@DmWidgetPanel.add(this)
  }

  /** Adds [cssClass] while [rule] holds. */
  fun cssClass(cssClass: String, rule: () -> Boolean) {
    add(CssClassRule(cssClass, rule))
  }

  /** Lets Ajax requests replace this widget. */
  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }

  /** The widget's style sheet, contributed to the page head; `null` for none. */
  protected open fun styleSheet(): ResourceReference? = null

  /** The widget's script, contributed to the page head after the core script; `null` for none. */
  protected open fun script(): ResourceReference? = null

  /** Adds the children the widget itself needs; runs before the author's `init`. */
  protected open fun addComponents() {}

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    addComponents()
    init()
  }

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { isVisible = it() }
    enabled?.let { isEnabled = it() }
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    styleSheet()?.let { response.render(CssHeaderItem.forReference(it)) }
    script()?.let {
      response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
      response.render(JavaScriptHeaderItem.forReference(it))
    }
  }
}
