package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.layout.Orientation
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** A node of a [DmOrganigram]: a name, a role, children. */
data class OrgNode
@JvmOverloads
constructor(val name: String, val role: String = "", val children: List<OrgNode> = emptyList()) : Serializable

/** Nodes in a hierarchy drawn top-down or left-right (requirement G.75): nested lists the sheet draws as a chart. */
open class DmOrganigram
@JvmOverloads
constructor(
  id: String,
  val root: OrgNode,
  var orientation: Orientation = Orientation.VERTICAL,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  override fun addComponents() {
    +NodePanel("root", root)
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("class", "dm-organigram-${orientation.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-organigram")

  /** One node with its children below it. */
  class NodePanel(id: String, private val node: OrgNode) : Panel(id) {
    override fun onInitialize() {
      super.onInitialize()
      add(Label("name", node.name))
      add(Label("role", node.role).apply { isVisible = node.role.isNotBlank() })
      add(object : ListView<OrgNode>("children", node.children) {
        override fun populateItem(item: ListItem<OrgNode>) {
          item.add(NodePanel("child", item.modelObject))
        }
      })
    }
  }
}
