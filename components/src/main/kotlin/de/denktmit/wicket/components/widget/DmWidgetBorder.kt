package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.behavior.CssClassRule
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.html.border.Border
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * Base of every border-shaped widget: a widget that wraps the markup the page author writes between its tags (group
 * box, card, input group, layout containers). Same hooks, rules and resource delivery as [DmWidgetPanel]; children
 * added with `+` go into the body, children the border itself needs go through [addToBorder].
 */
abstract class DmWidgetBorder
@JvmOverloads
constructor(
  id: String,
  model: IModel<*>? = null,
  @Transient
  private val init: DmWidgetBorder.() -> Unit = {},
) : Border(id, model) {

  /** Visibility rule, evaluated on every render; `null` leaves the Wicket default. */
  @Transient
  var visible: (() -> Boolean)? = null

  /** Enabled rule, evaluated on every render; `null` leaves the Wicket default. */
  @Transient
  var enabled: (() -> Boolean)? = null

  /** Adds a child to the body of this border, where the author's markup is. */
  operator fun Component.unaryPlus() {
    this@DmWidgetBorder.add(this)
  }

  /** Adds a behaviour to this border. */
  operator fun Behavior.unaryPlus() {
    this@DmWidgetBorder.add(this)
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

  /** Adds the children the border itself needs (with [addToBorder]); runs before the author's `init`. */
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
