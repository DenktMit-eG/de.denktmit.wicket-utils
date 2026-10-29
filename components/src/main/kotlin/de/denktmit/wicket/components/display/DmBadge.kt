package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import java.util.Optional

/** The colour a badge or tag speaks in. */
enum class Tone { NEUTRAL, INFO, SUCCESS, WARNING, DANGER }

/** A short mark on another widget (requirement G.70): a count or a state, in a [tone]. */
open class DmBadge
@JvmOverloads
constructor(
  id: String,
  model: IModel<String>,
  var tone: Tone = Tone.NEUTRAL,
) : Label(id, model) {

  @JvmOverloads
  constructor(id: String, text: String, tone: Tone = Tone.NEUTRAL) : this(id, Model.of(text), tone)

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("class", "dm-tone-${tone.name.lowercase()}", " ")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-display")))
  }
}

/**
 * A tag (requirement G.70): a labelled mark that may be removed by the user; [onRemove] is called through an Ajax
 * link with a plain link as fallback.
 */
open class DmTag
@JvmOverloads
constructor(
  id: String,
  model: IModel<String>,
  var tone: Tone = Tone.NEUTRAL,
  val removable: Boolean = false,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, model, init) {

  /** Called when the user removes the tag. */
  @Transient
  var onRemove: ((AjaxRequestTarget?) -> Unit)? = null

  override fun addComponents() {
    @Suppress("UNCHECKED_CAST")
    +Label("text", defaultModel as IModel<String>)
    +object : AjaxFallbackLink<Void>("remove") {
      override fun onClick(target: Optional<AjaxRequestTarget>) {
        onRemove?.invoke(target.orElse(null))
      }
    }.apply { isVisible = removable; add(DmIcon("icon", "close")) }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("class", "dm-tone-${tone.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-display")
}
