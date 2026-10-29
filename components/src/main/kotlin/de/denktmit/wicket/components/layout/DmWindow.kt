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
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable
import java.util.Optional

/** How a [DmWindow] sits on the page. */
enum class WindowMode { EMBEDDED, FLOATING, MODAL }

/**
 * A titled block with a body and an action footer (requirement C.20) that is embedded in the page, floats over it,
 * or blocks it as a modal dialog until closed. Floating and modal windows are the native `dialog` element: the
 * server renders its `open` state, so a window opened by a full request shows without script; script upgrades a
 * modal to `showModal()` (backdrop, focus trap, Escape) and makes the header drag the window. The close control is
 * an Ajax link with a plain link as fallback, so the server always knows the state. A window returns a [result] to
 * its opener: [close] with a value sets it and calls [onClose].
 *
 * The content is any component with id [CONTENT]; footer actions join through [action].
 */
open class DmWindow<R : Serializable>
@JvmOverloads
constructor(
  id: String,
  title: IModel<String>,
  var mode: WindowMode = WindowMode.MODAL,
  content: Component,
  /** The result the window reports when closed; `null` for closed without one. */
  val result: IModel<R?> = Model(),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, title, init) {

  private val content = content.also { require(it.id == CONTENT) { "content id must be '$CONTENT'" } }
  private val actions = RepeatingView("actions")

  /** Whether the window is open; an embedded window is always open. */
  var isOpen: Boolean = false
    private set

  /** Whether the user may drag the window (floating and modal). */
  var draggable: Boolean = true

  /** Whether the user may resize the window (floating and modal). */
  var resizable: Boolean = true

  /** Called with the result when the window closes. */
  @Transient
  var onClose: ((R?) -> Unit)? = null

  /** Adds a footer action; [factory] receives the id the component must use. */
  fun action(factory: (String) -> Component): DmWindow<R> {
    actions.add(factory(actions.newChildId()))
    return this
  }

  /** Opens the window and, with a [target], refreshes it in place. */
  @JvmOverloads
  fun open(target: AjaxRequestTarget? = null) {
    isOpen = true
    target?.add(this)
  }

  /** Closes the window with [value] as the result, calls [onClose] and, with a [target], refreshes it in place. */
  @JvmOverloads
  fun close(target: AjaxRequestTarget? = null, value: R? = null) {
    isOpen = false
    result.`object` = value
    onClose?.invoke(value)
    target?.add(this)
  }

  override fun addComponents() {
    outputMarkupId = true
    +object : WebMarkupContainer("dialog") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        when (mode) {
          WindowMode.EMBEDDED -> {
            tag.name = "section"
            tag.put("role", "region")
          }
          WindowMode.FLOATING, WindowMode.MODAL -> {
            tag.name = "dialog"
            if (isOpen) tag.put("open", "open") else tag.remove("open")
            if (mode == WindowMode.MODAL) tag.put("aria-modal", "true")
          }
        }
        tag.put("aria-labelledby", this@DmWindow.markupId + "-title")
        tag.put("data-dm-window", mode.name.lowercase())
        if (draggable && mode != WindowMode.EMBEDDED) tag.put("data-dm-window-draggable", "")
        if (resizable && mode != WindowMode.EMBEDDED) tag.append("class", "dm-window-resizable", " ")
      }
    }.apply {
      @Suppress("UNCHECKED_CAST")
      add(Label("title", this@DmWindow.defaultModel as IModel<String>).setMarkupId(this@DmWindow.markupId + "-title"))
      add(object : AjaxFallbackLink<Void>("close") {
        override fun onClick(target: Optional<AjaxRequestTarget>) {
          close(target.orElse(null))
        }

        override fun onConfigure() {
          super.onConfigure()
          isVisible = mode != WindowMode.EMBEDDED
        }
      }.apply { add(DmIcon("icon", "close")) })
      add(content)
      add(WebMarkupContainer("footer").apply {
        add(actions)
        add(object : org.apache.wicket.behavior.Behavior() {
          override fun onConfigure(component: Component) {
            component.isVisible = actions.size() > 0
          }
        })
      })
    }
  }

  override fun onConfigure() {
    super.onConfigure()
    if (mode == WindowMode.EMBEDDED) isOpen = true
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-window")

  override fun script(): ResourceReference? = if (mode == WindowMode.EMBEDDED) null else DmResources.js("dm-window")

  companion object {
    /** The id the content component must carry. */
    const val CONTENT = "content"
  }
}
