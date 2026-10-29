package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Page
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One step of a process shown by [DmStepBar]; a finished step may link to its page. */
data class Step
@JvmOverloads
constructor(
  val label: String,
  val page: Class<out Page>? = null,
  val parameters: PageParameters = PageParameters(),
) : Serializable

/**
 * The steps of a process (requirement C.29): the finished ones, the current one (announced with `aria-current`)
 * and the pending ones, with a link on every finished step that names a page. [current] is the index of the
 * current step; everything before it is finished.
 */
open class DmStepBar
@JvmOverloads
constructor(
  id: String,
  steps: IModel<List<Step>>,
  /** The index of the current step. */
  var current: Int = 0,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, steps, init) {

  @JvmOverloads
  constructor(id: String, steps: List<Step>, current: Int = 0, init: DmWidgetPanel.() -> Unit = {}) : this(id, Model.ofList(steps), current, init)

  /** The state of a step relative to [current]. */
  fun stateOf(index: Int): String = when {
    index < current -> "done"
    index == current -> "current"
    else -> "pending"
  }

  override fun addComponents() {
    @Suppress("UNCHECKED_CAST")
    +object : ListView<Step>("steps", defaultModel as IModel<List<Step>>) {
      override fun populateItem(item: ListItem<Step>) {
        val step = item.modelObject
        val state = stateOf(item.index)
        val linked = state == "done" && step.page != null
        item.add(object : BookmarkablePageLink<Void>("link", step.page ?: Page::class.java, step.parameters) {
          override fun onConfigure() {
            super.onConfigure()
            isVisible = linked
          }
        }.apply { add(Label("linkLabel", step.label)) })
        item.add(Label("label", step.label).apply { isVisible = !linked })
        item.add(DmIcon("done", "check").apply { isVisible = state == "done" })
        item.add(Label("number", (item.index + 1).toString()).apply { isVisible = state != "done" })
        item.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: org.apache.wicket.Component, tag: ComponentTag) {
            tag.append("class", "dm-step dm-step-$state", " ")
            if (state == "current") tag.put("aria-current", "step")
          }
        })
      }
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-step-bar")
}
