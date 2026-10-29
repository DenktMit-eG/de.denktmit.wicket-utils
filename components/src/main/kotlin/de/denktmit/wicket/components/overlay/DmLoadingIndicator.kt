package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.IAjaxIndicatorAware
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * The mark that the page or a region is busy (requirement H.83). Hidden until an Ajax request names it as its
 * indicator: Wicket's own script shows the element while the request runs and hides it after. The base page places
 * one for the whole page; a region places its own and attaches [LoadingIndicatorBehavior] to the Ajax component
 * ([indicates]). Without script nothing is busy, so the indicator never shows.
 */
open class DmLoadingIndicator
@JvmOverloads
constructor(
  id: String,
  label: IModel<String> = Model.of("Loading"),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, label, init) {

  override fun addComponents() {
    outputMarkupId = true
    @Suppress("UNCHECKED_CAST")
    +DmLabel("label", defaultModel as IModel<String>)
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("style", "display:none")
    tag.put("role", "status")
    tag.put("aria-live", "polite")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-loading-indicator")

  /** Binds the Ajax calls of [component] to this indicator. */
  fun indicates(component: Component) {
    component.add(LoadingIndicatorBehavior(this))
  }
}

/** Names [indicator] as the busy mark of the Ajax calls of the component this behaviour is attached to. */
class LoadingIndicatorBehavior(private val indicator: Component) : Behavior(), IAjaxIndicatorAware {
  override fun getAjaxIndicatorMarkupId(): String = indicator.markupId
}
