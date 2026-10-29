package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.behavior.visibleIf
import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * Tabs shown as an accordion (requirement C.17): every tab is a `details` element whose `summary` is the tab title,
 * so sections open and close without script; with [exclusive] the browser keeps one section open at a time. Every
 * panel is built up front, because every section may open.
 */
open class DmAccordion
@JvmOverloads
constructor(
  id: String,
  tabs: List<DmTabItem>,
  /** One open section at a time. */
  var exclusive: Boolean = true,
  /** The index of the section open at first, `-1` for none. */
  var open: Int = 0,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, Model.ofList(tabs), init) {

  override fun addComponents() {
    outputMarkupId = true
    @Suppress("UNCHECKED_CAST")
    +object : ListView<DmTabItem>("items", defaultModel as org.apache.wicket.model.IModel<List<DmTabItem>>) {
      override fun populateItem(item: ListItem<DmTabItem>) {
        val tab = item.modelObject
        item.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: org.apache.wicket.Component, tag: ComponentTag) {
            if (item.index == open) tag.put("open", "open")
            if (exclusive) tag.put("name", this@DmAccordion.markupId)
          }
        })
        item.add(DmIcon("icon", tab.icon ?: "").visibleIf { tab.icon != null })
        item.add(Label("title", tab.title))
        item.add(Label("badge", tab.badge ?: Model.of("")).visibleIf { tab.badge != null })
        item.add(tab.getPanel("panel"))
        item.isVisible = tab.isVisible
      }
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-accordion")
}
