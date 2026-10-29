package de.denktmit.wicket.components.overlay

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
import org.apache.wicket.request.resource.ResourceReference
import java.util.Optional

/** One step of a [DmCoachMark]: the widget to highlight and the explanation. */
class CoachStep(val target: Component, val title: String, val text: String) : java.io.Serializable {
  init {
    target.outputMarkupId = true
  }
}

/**
 * A highlighted widget with an explanation, in a sequence the user steps through (requirement H.82): a dialog
 * with the current step's text, next, back and done links (Ajax with fallback), and script that outlines the
 * step's widget and places the dialog next to it. Without script the dialog shows in the page flow and the links
 * step through it all the same.
 */
open class DmCoachMark
@JvmOverloads
constructor(
  id: String,
  val steps: List<CoachStep>,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  /** The index of the step shown. */
  var current: Int = 0
    private set

  /** Whether the sequence is running. */
  var isOpen: Boolean = false
    private set

  /** Called when the user finishes or leaves the sequence. */
  @Transient
  var onDone: (() -> Unit)? = null

  /** Starts at the first step; with a [target] in place. */
  @JvmOverloads
  fun start(target: AjaxRequestTarget? = null) {
    current = 0
    isOpen = steps.isNotEmpty()
    target?.add(this)
  }

  private fun step(): CoachStep = steps[current]

  private fun link(id: String, action: () -> Unit) = object : AjaxFallbackLink<Void>(id) {
    override fun onClick(target: Optional<AjaxRequestTarget>) {
      action()
      target.ifPresent { it.add(this@DmCoachMark) }
    }
  }

  override fun addComponents() {
    outputMarkupId = true
    +object : WebMarkupContainer("dialog") {
      override fun onConfigure() {
        super.onConfigure()
        isVisible = isOpen
      }

      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("open", "open")
        tag.put("data-dm-coach-target", step().target.markupId)
      }
    }.apply {
      add(Label("title", object : IModel<String> { override fun getObject() = step().title }))
      add(Label("text", object : IModel<String> { override fun getObject() = step().text }))
      add(Label("position", object : IModel<String> { override fun getObject() = "${current + 1} of ${steps.size}" }))
      add(link("back") { if (current > 0) current-- }.apply { add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: Component) {
          component.isEnabled = current > 0
        }
      }) })
      add(link("next") { if (current < steps.size - 1) current++ }.apply { add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: Component) {
          component.isVisible = current < steps.size - 1
        }
      }) })
      add(link("done") { isOpen = false; onDone?.invoke() }.apply { add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: Component) {
          component.isVisible = current == steps.size - 1
        }
      }) })
      add(link("close") { isOpen = false; onDone?.invoke() }.apply { add(DmIcon("icon", "close")) })
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-coach-mark", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-coach-mark")

  override fun script(): ResourceReference = DmResources.js("dm-coach-mark")
}
