package de.denktmit.wicket.components.navigation

import org.apache.wicket.Page
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.request.mapper.parameter.PageParameters
import java.io.Serializable

/** What a [MenuItem] is. */
enum class MenuItemKind { ACTION, CHECK, RADIO, SEPARATOR }

/**
 * One entry of a menu (requirements D.31, D.32): a label with an optional icon and shortcut, an action or a page
 * to go to, children for a sub-menu, a check or radio state, and whether it is enabled. The same model serves the
 * menu bar and the popup menu. [checked] changes when the user toggles a check or radio item; [action] runs after.
 */
class MenuItem
@JvmOverloads
constructor(
  val label: String,
  val kind: MenuItemKind = MenuItemKind.ACTION,
  /** A symbol name from the sprite, `null` for none. */
  val icon: String? = null,
  /** The shortcut as shown and bound, like `Ctrl+S`; `null` for none. */
  val shortcut: String? = null,
  /** The page the item goes to; a plain link, no action. */
  val page: Class<out Page>? = null,
  val parameters: PageParameters = PageParameters(),
  var enabled: Boolean = true,
  var checked: Boolean = false,
  /** The radio group a radio item belongs to; checking one unchecks its siblings of the same group. */
  val group: String? = null,
  val children: List<MenuItem> = emptyList(),
  @Transient
  val action: ((AjaxRequestTarget?) -> Unit)? = null,
) : Serializable {

  /** Whether this item opens a sub-menu. */
  val hasChildren: Boolean get() = children.isNotEmpty()

  companion object {
    /** A separator between groups of items. */
    @JvmStatic
    fun separator(): MenuItem = MenuItem("", MenuItemKind.SEPARATOR)

    /** An action item. */
    @JvmStatic
    @JvmOverloads
    fun action(label: String, icon: String? = null, shortcut: String? = null, enabled: Boolean = true, action: (AjaxRequestTarget?) -> Unit): MenuItem =
      MenuItem(label, MenuItemKind.ACTION, icon, shortcut, enabled = enabled, action = action)

    /** A link to a page. */
    @JvmStatic
    @JvmOverloads
    fun page(label: String, page: Class<out Page>, parameters: PageParameters = PageParameters(), icon: String? = null): MenuItem =
      MenuItem(label, MenuItemKind.ACTION, icon, page = page, parameters = parameters)

    /** A check item. */
    @JvmStatic
    @JvmOverloads
    fun check(label: String, checked: Boolean = false, action: ((AjaxRequestTarget?) -> Unit)? = null): MenuItem =
      MenuItem(label, MenuItemKind.CHECK, checked = checked, action = action)

    /** A radio item of [group]. */
    @JvmStatic
    @JvmOverloads
    fun radio(label: String, group: String, checked: Boolean = false, action: ((AjaxRequestTarget?) -> Unit)? = null): MenuItem =
      MenuItem(label, MenuItemKind.RADIO, checked = checked, group = group, action = action)

    /** A sub-menu. */
    @JvmStatic
    @JvmOverloads
    fun menu(label: String, icon: String? = null, children: List<MenuItem>): MenuItem = MenuItem(label, MenuItemKind.ACTION, icon, children = children)
  }
}
