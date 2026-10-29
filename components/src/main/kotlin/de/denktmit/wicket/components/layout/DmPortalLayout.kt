package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.CallbackParameter
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * Columns of panels the user drags between columns and reorders (requirement C.22). The order is the model: a list
 * of columns, each a list of panel keys; [panel] builds the panel for a key with the id it must use. Script drags
 * panels and reports the new order to the server, which stores it in the model and calls [onReorder]; without
 * script every panel carries move links that do the same by a full request.
 */
open class DmPortalLayout
@JvmOverloads
constructor(
  id: String,
  order: IModel<List<List<String>>>,
  @Transient
  private val panel: (key: String, id: String) -> Component,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, order, init) {

  /** Called after the order changed, with the new order. */
  @Transient
  var onReorder: ((List<List<String>>) -> Unit)? = null

  @Suppress("UNCHECKED_CAST")
  private val order: IModel<List<List<String>>> get() = defaultModel as IModel<List<List<String>>>

  /** The current order: columns of panel keys. */
  val columns: List<List<String>> get() = order.`object`

  /** Moves the panel [key] to [column] at [index]; reports the change through [onReorder]. */
  fun move(key: String, column: Int, index: Int) {
    val next = columns.map { it.filterNot { k -> k == key }.toMutableList() }.toMutableList()
    val target = column.coerceIn(0, next.size - 1)
    next[target].add(index.coerceIn(0, next[target].size), key)
    order.`object` = next
    onReorder?.invoke(next)
  }

  private val reorder = object : AbstractDefaultAjaxBehavior() {
    override fun respond(target: AjaxRequestTarget) {
      val encoded = requestCycle.request.requestParameters.getParameterValue("order").toString("")
      val next = encoded.split("|").map { column -> column.split(",").filter { it.isNotBlank() } }
      order.`object` = next
      onReorder?.invoke(next)
      target.add(this@DmPortalLayout)
    }
  }

  override fun addComponents() {
    outputMarkupId = true
    add(reorder)
    +object : ListView<List<String>>("columns", order) {
      override fun populateItem(column: ListItem<List<String>>) {
        val columnIndex = column.index
        column.add(object : ListView<String>("panels", column.model) {
          override fun populateItem(item: ListItem<String>) {
            val key = item.modelObject
            item.add(org.apache.wicket.AttributeModifier.replace("data-dm-portal-panel", key))
            item.add(moveLink("up") { move(key, columnIndex, item.index - 1) }.apply { add(DmIcon("icon", "chevron-up")) })
            item.add(moveLink("down") { move(key, columnIndex, item.index + 1) }.apply { add(DmIcon("icon", "chevron-down")) })
            item.add(moveLink("prev") { move(key, columnIndex - 1, item.index) }.apply { add(DmIcon("icon", "chevron-left")) })
            item.add(moveLink("next") { move(key, columnIndex + 1, item.index) }.apply { add(DmIcon("icon", "chevron-right")) })
            item.add(panel(key, "panel"))
          }
        })
      }
    }
  }

  private fun moveLink(id: String, action: () -> Unit) = object : Link<Void>(id) {
    override fun onClick() = action()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-portal", "")
    tag.put("data-dm-portal-callback", reorder.getCallbackFunction(CallbackParameter.explicit("order")))
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-portal-layout")

  override fun script(): ResourceReference = DmResources.js("dm-portal-layout")
}
