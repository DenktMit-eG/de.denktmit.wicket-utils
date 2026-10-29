package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.display.DmIcon
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model
import java.util.Optional

/**
 * One level of a menu: the items as `menuitem`, `menuitemcheckbox` or `menuitemradio` links, separators, and a
 * nested [MenuPanel] per sub-menu. Actions run through an Ajax link with a plain link as fallback; page items are
 * bookmarkable links. Used by [DmMenuBar] and [DmPopupMenu].
 */
class MenuPanel(
  id: String,
  private val items: List<MenuItem>,
  private val all: List<MenuItem>,
  private val root: Component,
  private val bar: Boolean,
) : Panel(id) {

  override fun onInitialize() {
    super.onInitialize()
    add(object : ListView<MenuItem>("items", items) {
      override fun populateItem(item: ListItem<MenuItem>) {
        val entry = item.modelObject
        val separator = entry.kind == MenuItemKind.SEPARATOR
        item.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: Component, tag: ComponentTag) {
            tag.put("role", if (separator) "separator" else "none")
            if (entry.hasChildren) tag.append("class", "dm-menu-parent", " ")
          }
        })
        val link: WebMarkupContainer = when {
          entry.page != null -> pageLink("link", entry.page, entry.parameters)
          else -> object : AjaxFallbackLink<Void>("link") {
            override fun onClick(target: Optional<AjaxRequestTarget>) {
              activate(entry)
              entry.action?.invoke(target.orElse(null))
              target.ifPresent { it.add(root) }
            }
          }
        }
        link.isVisible = !separator
        link.isEnabled = entry.enabled
        link.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: Component, tag: ComponentTag) {
            tag.put("role", when (entry.kind) { MenuItemKind.CHECK -> "menuitemcheckbox"; MenuItemKind.RADIO -> "menuitemradio"; else -> "menuitem" })
            if (entry.kind == MenuItemKind.CHECK || entry.kind == MenuItemKind.RADIO) tag.put("aria-checked", entry.checked.toString())
            if (!entry.enabled) tag.put("aria-disabled", "true")
            if (entry.hasChildren) {
              tag.put("aria-haspopup", "menu")
              tag.put("aria-expanded", "false")
            }
            entry.shortcut?.let { tag.put("data-dm-shortcut", it) }
            tag.put("tabindex", if (bar && item.index == 0 && findParent(MenuPanel::class.java) == null) "0" else "-1")
          }
        })
        link.add(DmIcon("icon", entry.icon ?: "").apply { isVisible = entry.icon != null })
        link.add(Label("label", entry.label))
        link.add(Label("shortcut", entry.shortcut ?: "").apply { isVisible = entry.shortcut != null })
        link.add(DmIcon("check", "check").apply { isVisible = entry.kind != MenuItemKind.ACTION })
        item.add(link)
        item.add(if (entry.hasChildren) MenuPanel("submenu", entry.children, all, root, false) else WebMarkupContainer("submenu").apply { isVisible = false })
      }
    })
  }

  private fun activate(entry: MenuItem) {
    when (entry.kind) {
      MenuItemKind.CHECK -> entry.checked = !entry.checked
      MenuItemKind.RADIO -> {
        radios(all).filter { it.group == entry.group }.forEach { it.checked = false }
        entry.checked = true
      }
      else -> {}
    }
  }

  private fun radios(items: List<MenuItem>): List<MenuItem> = items.flatMap { listOf(it) + radios(it.children) }.filter { it.kind == MenuItemKind.RADIO }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", if (bar) "menubar" else "menu")
  }
}
