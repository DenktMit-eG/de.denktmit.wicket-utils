package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.behavior.visibleIf
import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.LoopItem
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import java.util.Optional

/**
 * Tabs with one visible panel (requirement C.17), a thin layer over Wicket's `TabbedPanel`: tabs may carry an icon
 * and a badge, may be closable, and the strip sits on any [side]. Tabs switch by link (a full request) or, with
 * [ajax], by an Ajax request that falls back to the link without script. The tab strip is a `tablist`, every tab a
 * `tab` with its selected state announced, the panel a `tabpanel`. Closing a tab removes it from [getTabs] and
 * calls [onClose]. For the accordion presentation see [DmAccordion].
 */
open class DmTabBox
@JvmOverloads
constructor(
  id: String,
  tabs: List<DmTabItem>,
  /** Where the tab strip sits. */
  var side: Side = Side.TOP,
  /** Switch tabs by Ajax with the link as fallback. */
  var ajax: Boolean = false,
  selected: IModel<Int>? = null,
) : TabbedPanel<DmTabItem>(id, ArrayList(tabs), selected) {

  /** Called after a tab was closed. */
  @Transient
  var onClose: ((DmTabItem) -> Unit)? = null

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    outputMarkupId = true
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("class", "dm-tab-box-${side.name.lowercase()}", " ")
  }

  override fun newTabsContainer(id: String): WebMarkupContainer = object : WebMarkupContainer(id) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("role", "tablist")
      tag.put("aria-orientation", if (side == Side.START || side == Side.END) "vertical" else "horizontal")
    }
  }

  override fun newTabContainer(index: Int): LoopItem {
    val item = super.newTabContainer(index)
    item.add(object : Link<Void>("close") {
      override fun onClick() {
        closeTab(index)
      }
    }.visibleIf { tabs[index].closable }.apply { add(DmIcon("icon", "close")) })
    return item
  }

  override fun newTitle(titleId: String, titleModel: IModel<*>, index: Int): Component = TabTitle(titleId, tabs[index])

  override fun newLink(linkId: String, index: Int): WebMarkupContainer {
    val link: WebMarkupContainer = if (ajax) {
      object : AjaxFallbackLink<Void>(linkId) {
        override fun onClick(target: Optional<AjaxRequestTarget>) {
          setSelectedTab(index)
          target.ifPresent { it.add(this@DmTabBox) }
        }
      }
    } else {
      object : Link<Void>(linkId) {
        override fun onClick() {
          setSelectedTab(index)
        }
      }
    }
    link.add(object : Behavior() {
      override fun onComponentTag(component: Component, tag: ComponentTag) {
        tag.put("role", "tab")
        tag.put("aria-selected", (selectedTab == index).toString())
      }
    })
    return link
  }

  /** Removes the tab at [index] and selects a neighbour when the selected one goes. */
  fun closeTab(index: Int) {
    val tab = tabs.removeAt(index)
    if (tabs.isNotEmpty() && selectedTab >= tabs.size) setSelectedTab(tabs.size - 1) else if (tabs.isNotEmpty()) setSelectedTab(minOf(selectedTab, tabs.size - 1))
    onClose?.invoke(tab)
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-tab-box")))
  }

  /** Icon, title and badge of one tab. */
  class TabTitle(id: String, tab: DmTabItem) : Panel(id) {
    init {
      add(DmIcon("icon", tab.icon ?: "").visibleIf { tab.icon != null })
      add(Label("text", tab.title))
      add(Label("badge", tab.badge ?: org.apache.wicket.model.Model.of("")).visibleIf { tab.badge != null })
    }
  }
}
