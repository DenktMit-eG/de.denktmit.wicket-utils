package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.navigation.paging.IPageable
import org.apache.wicket.markup.html.navigation.paging.IPageableItems
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference
import java.util.Optional

/**
 * Page navigation for any pageable (requirement D.36), Wicket's `PagingNavigator` with first, previous, numbered,
 * next and last links, plus the page size and the total count where the pageable knows its items
 * (`IPageableItems`). By link, or by Ajax with the link as fallback when [ajax] is on; [refresh] names the
 * component the Ajax variant updates besides the pager (the table or list).
 */
open class DmPager
@JvmOverloads
constructor(
  id: String,
  private val pageable: IPageable,
  val ajax: Boolean = false,
  /** The page sizes offered; empty for none. */
  val pageSizes: List<Int> = listOf(10, 25, 50),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private var refresh: org.apache.wicket.Component? = null

  /** The component to refresh by Ajax together with the pager, normally the collection widget. */
  fun refresh(component: org.apache.wicket.Component): DmPager {
    component.outputMarkupId = true
    refresh = component
    return this
  }

  private val items: IPageableItems? get() = pageable as? IPageableItems

  override fun addComponents() {
    outputMarkupId = true
    +if (ajax) {
      object : AjaxPagingNavigator("navigator", pageable) {
        override fun onAjaxEvent(target: AjaxRequestTarget) {
          target.add(this@DmPager)
          refresh?.let { target.add(it) }
        }
      }
    } else {
      PagingNavigator("navigator", pageable)
    }
    +Label("total", object : IModel<String> {
      override fun getObject(): String = items?.let { "${it.itemCount} items" } ?: ""
    }).apply { add(object : org.apache.wicket.behavior.Behavior() {
      override fun onConfigure(component: org.apache.wicket.Component) {
        component.isVisible = items != null
      }
    }) }
    +object : ListView<Int>("sizes", pageSizes) {
      override fun populateItem(item: ListItem<Int>) {
        val size = item.modelObject
        item.add(object : AjaxFallbackLink<Void>("size") {
          override fun onClick(target: Optional<AjaxRequestTarget>) {
            items?.itemsPerPage = size.toLong()
            pageable.currentPage = 0
            target.ifPresent { it.add(this@DmPager); refresh?.let { r -> it.add(r) } }
          }

          override fun onConfigure() {
            super.onConfigure()
            isEnabled = items?.itemsPerPage != size.toLong()
          }

          override fun onComponentTag(tag: ComponentTag) {
            super.onComponentTag(tag)
            if (items?.itemsPerPage == size.toLong()) tag.put("aria-current", "true")
          }
        }.apply { add(Label("sizeLabel", size.toString())) })
      }

      override fun onConfigure() {
        super.onConfigure()
        isVisible = items != null && pageSizes.isNotEmpty()
      }
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", "navigation")
    tag.put("aria-label", "Pages")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-pager")
}
