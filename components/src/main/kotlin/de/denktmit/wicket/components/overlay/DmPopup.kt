package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/** What opens a [DmPopup]. */
enum class PopupTrigger { CLICK, HOVER }

/**
 * A floating panel anchored to a widget (requirement H.80): a `details` element whose `summary` is the anchor
 * text, so it opens and closes without script; [anchorTo] binds it to another widget by script, opened on click
 * or hover, closed on a click outside or Escape. The content is any component with id [CONTENT].
 */
open class DmPopup
@JvmOverloads
constructor(
  id: String,
  content: Component,
  val label: IModel<String> = Model.of("More"),
  var trigger: PopupTrigger = PopupTrigger.CLICK,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private val content = content.also { require(it.id == CONTENT) { "content id must be '$CONTENT'" } }
  private var anchor: Component? = null

  /** Whether the popup is open on the next render. */
  var isOpen: Boolean = false

  /** Opens the popup from [component] as well (click or hover as [trigger] says). */
  fun anchorTo(component: Component): DmPopup {
    component.outputMarkupId = true
    anchor = component
    return this
  }

  override fun addComponents() {
    outputMarkupId = true
    +object : WebMarkupContainer("details") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        if (isOpen) tag.put("open", "open") else tag.remove("open")
      }
    }.apply {
      add(Label("label", label))
      add(content)
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-popup", trigger.name.lowercase())
    anchor?.let { tag.put("data-dm-popup-for", it.markupId) }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-popup")

  override fun script(): ResourceReference = DmResources.js("dm-popup")

  companion object {
    /** The id of the content component. */
    const val CONTENT = "content"
  }
}
