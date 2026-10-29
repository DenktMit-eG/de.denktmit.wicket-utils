package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/**
 * A short text on hover and on focus with a delay (requirement H.81): the `title` attribute is the no-script
 * form the browser shows itself; script replaces it with a styled tooltip that appears after [delayMillis] and is
 * announced through `aria-describedby`.
 */
class TooltipBehavior
@JvmOverloads
constructor(
  private val text: IModel<String>,
  private val delayMillis: Int = 400,
) : Behavior() {

  @JvmOverloads
  constructor(text: String, delayMillis: Int = 400) : this(Model.of(text), delayMillis)

  override fun onComponentTag(component: Component, tag: ComponentTag) {
    tag.put("title", text.`object`)
    tag.put("data-dm-tooltip", text.`object`)
    tag.put("data-dm-tooltip-delay", delayMillis.toString())
  }

  override fun renderHead(component: Component, response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-tooltip")))
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
    response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-tooltip")))
  }
}

/** Gives this component a tooltip. */
@JvmOverloads
fun <C : Component> C.tooltip(text: String, delayMillis: Int = 400): C = apply { add(TooltipBehavior(text, delayMillis)) }
