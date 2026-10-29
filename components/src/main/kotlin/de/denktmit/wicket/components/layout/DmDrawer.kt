package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import java.util.Optional

/**
 * A panel that slides in from an edge over the page (requirement C.21) and closes on its close control, on Escape
 * or on a click outside. A native `dialog` with its `open` state rendered by the server; script upgrades it to a
 * modal and wires the click outside to the close link, which is an Ajax link with a plain link as fallback. The
 * content is any component with id [CONTENT].
 */
open class DmDrawer
@JvmOverloads
constructor(
  id: String,
  title: IModel<String>,
  var side: Side = Side.END,
  content: Component,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, title, init) {

  @JvmOverloads
  constructor(id: String, title: String, side: Side = Side.END, content: Component, init: DmWidgetPanel.() -> Unit = {}) :
    this(id, Model.of(title), side, content, init)

  private val content = content.also { require(it.id == CONTENT) { "content id must be '$CONTENT'" } }

  /** Whether the drawer is open. */
  var isOpen: Boolean = false
    private set

  /** Called when the drawer closes. */
  @Transient
  var onClose: (() -> Unit)? = null

  /** Opens the drawer and, with a [target], refreshes it in place. */
  @JvmOverloads
  fun open(target: AjaxRequestTarget? = null) {
    isOpen = true
    target?.add(this)
  }

  /** Closes the drawer, calls [onClose] and, with a [target], refreshes it in place. */
  @JvmOverloads
  fun close(target: AjaxRequestTarget? = null) {
    isOpen = false
    onClose?.invoke()
    target?.add(this)
  }

  override fun addComponents() {
    outputMarkupId = true
    +object : WebMarkupContainer("dialog") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        if (isOpen) tag.put("open", "open") else tag.remove("open")
        tag.put("aria-modal", "true")
        tag.put("aria-labelledby", this@DmDrawer.markupId + "-title")
        tag.put("data-dm-drawer", side.name.lowercase())
        tag.append("class", "dm-drawer-${side.name.lowercase()}", " ")
      }
    }.apply {
      @Suppress("UNCHECKED_CAST")
      add(Label("title", this@DmDrawer.defaultModel as IModel<String>).setMarkupId(this@DmDrawer.markupId + "-title"))
      add(object : AjaxFallbackLink<Void>("close") {
        override fun onClick(target: Optional<AjaxRequestTarget>) {
          close(target.orElse(null))
        }
      }.apply { add(DmIcon("icon", "close")) })
      add(content)
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-drawer")

  override fun script(): ResourceReference = DmResources.js("dm-drawer")

  companion object {
    /** The id the content component must carry. */
    const val CONTENT = "content"
  }
}
