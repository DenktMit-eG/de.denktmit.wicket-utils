package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetBorder
import org.apache.wicket.Component
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * An input with a prefix, a suffix, a unit or a button attached to it (requirement E.54): a border around the
 * author's input; [prefix] and [suffix] are texts (a currency sign, a unit), [button] any component with id
 * [BUTTON] placed after the input: a link, a label or a panel that brings its own button markup.
 */
open class DmInputGroup
@JvmOverloads
constructor(
  id: String,
  private val prefix: IModel<String>? = null,
  private val suffix: IModel<String>? = null,
  init: DmWidgetBorder.() -> Unit = {},
) : DmWidgetBorder(id, null, init) {

  @JvmOverloads
  constructor(id: String, prefix: String?, suffix: String?, init: DmWidgetBorder.() -> Unit = {}) :
    this(id, prefix?.let { Model.of(it) }, suffix?.let { Model.of(it) }, init)

  private var button: Component = WebMarkupContainer(BUTTON).apply { isVisible = false }

  /** Places [component] (id [BUTTON]) after the input. */
  fun button(component: Component): DmInputGroup {
    require(component.id == BUTTON) { "button id must be '$BUTTON'" }
    button = component
    return this
  }

  override fun addComponents() {
    addToBorder(Label("prefix", prefix ?: Model.of("")).apply { isVisible = prefix != null })
    addToBorder(Label("suffix", suffix ?: Model.of("")).apply { isVisible = suffix != null })
    addToBorder(button)
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-input-group")

  companion object {
    /** The id of the button component. */
    const val BUTTON = "button"
  }
}
