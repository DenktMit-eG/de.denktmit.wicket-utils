package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetBorder
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A titled section around the author's markup (requirement C.18): a `fieldset` with a `legend`, or, when
 * [collapsible], a `details` with the title as `summary`, which opens and closes without script. [open] is the
 * initial state of a collapsible box.
 */
open class DmGroupBox
@JvmOverloads
constructor(
  id: String,
  title: IModel<String>,
  var collapsible: Boolean = false,
  var open: Boolean = true,
  init: DmWidgetBorder.() -> Unit = {},
) : DmWidgetBorder(id, title, init) {

  @JvmOverloads
  constructor(id: String, title: String, collapsible: Boolean = false, open: Boolean = true, init: DmWidgetBorder.() -> Unit = {}) :
    this(id, Model.of(title), collapsible, open, init)

  override fun addComponents() {
    val shell = object : WebMarkupContainer("shell") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        if (collapsible) {
          tag.name = "details"
          if (open) tag.put("open", "open") else tag.remove("open")
        } else {
          tag.name = "fieldset"
          tag.remove("open")
        }
      }
    }
    @Suppress("UNCHECKED_CAST")
    val title = object : Label("title", defaultModel as IModel<String>) {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.name = if (collapsible) "summary" else "legend"
      }
    }
    shell.add(title)
    addToBorder(shell)
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-group-box")
}
