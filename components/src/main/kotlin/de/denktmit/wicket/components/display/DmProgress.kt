package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebComponent
import org.apache.wicket.model.IModel

/**
 * A value as a bar (requirement G.69): the native `progress` element, determinate with the value shown as text
 * inside it, indeterminate while the model is `null`. [max] is the full scale.
 */
open class DmProgress
@JvmOverloads
constructor(
  id: String,
  model: IModel<Double?>,
  val max: Double = 100.0,
  var showValue: Boolean = true,
  val label: IModel<String>? = null,
) : WebComponent(id, model) {

  private val value: Double? get() = defaultModelObject as Double?

  /** The value as text: a percentage of [max]. */
  fun text(): String = value?.let { "${Math.round(it / max * 100)} %" } ?: ""

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.name = "progress"
    tag.put("max", max.toString())
    value?.let { tag.put("value", it.toString()) } ?: tag.remove("value")
    tag.put("aria-label", label?.`object` ?: "Progress")
    if (value != null && showValue) tag.put("data-dm-progress-value", text())
    if (value == null) tag.append("class", "dm-progress-indeterminate", " ")
  }

  override fun onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    replaceComponentTagBody(markupStream, openTag, if (showValue) text() else "")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-display")))
  }
}
