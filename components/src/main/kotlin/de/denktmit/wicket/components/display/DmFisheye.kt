package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.navigation.pageLink
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Page
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One icon of a [DmFisheye]: a symbol, its label and the page it leads to. */
data class FisheyeItem
@JvmOverloads
constructor(val icon: String, val label: String, val page: Class<out Page>, val parameters: PageParameters = PageParameters()) : Serializable

/** A row of icons that enlarge on hover (requirement G.76), each a page link with its label as its accessible name. */
open class DmFisheye
@JvmOverloads
constructor(
  id: String,
  val items: List<FisheyeItem>,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  override fun addComponents() {
    +object : ListView<FisheyeItem>("items", items) {
      override fun populateItem(item: ListItem<FisheyeItem>) {
        val entry = item.modelObject
        item.add(pageLink("link", entry.page, entry.parameters).apply {
          add(org.apache.wicket.AttributeModifier.replace("aria-label", entry.label))
          add(DmIcon("icon", entry.icon))
          add(Label("label", entry.label))
        })
      }
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-display")
}
