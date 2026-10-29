package de.denktmit.wicket.components.collection

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.markup.repeater.data.DataView
import org.apache.wicket.markup.repeater.data.IDataProvider
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable
import java.util.Optional

/**
 * A list over a source too large to render at once (requirement F.61): the first [pageSize] rows render, a
 * "load more" link appends the next batch, and script clicks that link when it scrolls into view, so rows load
 * as the user scrolls. Without script the link does the same by hand. The source does the paging; nothing loads
 * every row.
 */
open class DmLargeList<T : Serializable>
@JvmOverloads
constructor(
  id: String,
  val source: IDataProvider<T>,
  @Transient
  private val template: (String, IModel<T>) -> Component,
  val pageSize: Int = 50,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  /** How many rows are shown. */
  var loaded: Int = pageSize
    private set

  /** Whether the source has more rows than shown. */
  fun hasMore(): Boolean = source.size() > loaded

  /** Shows the next batch. */
  fun loadMore() {
    loaded += pageSize
  }

  override fun addComponents() {
    outputMarkupId = true
    +object : DataView<T>("items", source) {
      override fun onBeforeRender() {
        itemsPerPage = loaded.toLong()
        currentPage = 0
        super.onBeforeRender()
      }

      override fun populateItem(item: Item<T>) {
        item.add(template("item", item.model))
      }
    }
    +object : AjaxFallbackLink<Void>("more") {
      override fun onClick(target: Optional<AjaxRequestTarget>) {
        loadMore()
        target.ifPresent { it.add(this@DmLargeList) }
      }

      override fun onConfigure() {
        super.onConfigure()
        isVisible = hasMore()
      }
    }.apply { add(Label("moreLabel", object : IModel<String> { override fun getObject(): String = "Load more (${source.size() - loaded} left)" })) }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-large-list", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-list")

  override fun script(): ResourceReference = DmResources.js("dm-large-list")
}
