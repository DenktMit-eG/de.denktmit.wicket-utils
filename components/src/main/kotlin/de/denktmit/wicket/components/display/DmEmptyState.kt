package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A block that explains that there is nothing to show and offers the next action (requirement G.74): an icon, a
 * title, a text and an optional action component (id [ACTION]), a link or a button the author brings.
 */
open class DmEmptyState
@JvmOverloads
constructor(
  id: String,
  val title: IModel<String>,
  val text: IModel<String> = Model.of(""),
  val icon: String = "empty",
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, title, init) {

  @JvmOverloads
  constructor(id: String, title: String, text: String = "", icon: String = "empty", init: DmWidgetPanel.() -> Unit = {}) : this(id, Model.of(title), Model.of(text), icon, init)

  private var action: Component = WebMarkupContainer(ACTION).apply { isVisible = false }

  /** Places [component] (id [ACTION]) as the next action. */
  fun action(component: Component): DmEmptyState {
    require(component.id == ACTION) { "action id must be '$ACTION'" }
    action = component
    return this
  }

  override fun addComponents() {
    +DmIcon("icon", icon)
    +Label("title", title)
    +Label("text", text).apply { isVisible = text.`object`.isNotBlank() }
    +action
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-display")

  companion object {
    /** The id of the action component. */
    const val ACTION = "action"
  }
}
