package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.behavior.CssClassRule
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.FormComponentPanel
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * Base of every composite input of the catalogue (switch, slider, rating, chosen box, cascader, ...): a
 * `FormComponentPanel` whose value is read from one or more native inputs so that the form submits without script
 * (A.2), with the stable hooks (A.3), rules (I.89, I.90) and resource delivery of [DmWidgetPanel].
 *
 * A subclass creates its inner inputs in [addComponents] and reads them in [convertInput]; the label of the whole
 * input is the label the form binds to the first inner input ([labelTarget]).
 */
abstract class DmInputPanel<T>
@JvmOverloads
constructor(
  id: String,
  model: IModel<T>? = null,
  @Transient
  private val init: DmInputPanel<T>.() -> Unit = {},
) : FormComponentPanel<T>(id, model) {

  /** Visibility rule, evaluated on every render; `null` leaves the Wicket default. */
  @Transient
  var visible: (() -> Boolean)? = null

  /** Enabled rule, evaluated on every render; `null` leaves the Wicket default. */
  @Transient
  var enabled: (() -> Boolean)? = null

  /** Adds a child to this input. */
  operator fun Component.unaryPlus() {
    this@DmInputPanel.add(this)
  }

  /** Adds a behaviour to this input. */
  operator fun Behavior.unaryPlus() {
    this@DmInputPanel.add(this)
  }

  /** Adds [cssClass] while [rule] holds. */
  fun cssClass(cssClass: String, rule: () -> Boolean) {
    add(CssClassRule(cssClass, rule))
  }

  /** Lets Ajax requests replace this input. */
  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }

  /** The inner input a `<label for>` should point at, or `null` when the widget carries its own label. */
  open fun labelTarget(): FormComponent<*>? = null

  /** The widget's style sheet, contributed to the page head; `null` for none. */
  protected open fun styleSheet(): ResourceReference? = null

  /** The widget's script, contributed to the page head after the core script; `null` for none. */
  protected open fun script(): ResourceReference? = null

  /** Adds the inner inputs; runs before the author's `init`. */
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
