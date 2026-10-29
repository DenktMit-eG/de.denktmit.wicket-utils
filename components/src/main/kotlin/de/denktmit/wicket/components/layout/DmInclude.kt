package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.link.InlineFrame
import org.apache.wicket.request.mapper.parameter.PageParameters

/**
 * A region whose content is another panel or page chosen at runtime (requirement C.26): [content] places any
 * component (its id must be [CONTENT]), [page] embeds a page in an inline frame. The choice can change between
 * renders, so a page swaps the region's content with [content] and an Ajax refresh.
 */
open class DmInclude
@JvmOverloads
constructor(
  id: String,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private fun empty(id: String): Component = WebMarkupContainer(id).apply { isVisible = false }

  override fun addComponents() {
    outputMarkupId = true
    if (get(CONTENT) == null) add(empty(CONTENT))
    if (get(PAGE) == null) add(empty(PAGE))
  }

  /** Shows [component] in the region; its id must be [CONTENT]. */
  fun content(component: Component): DmInclude {
    require(component.id == CONTENT) { "content id must be '$CONTENT', was '${component.id}'" }
    addOrReplace(component, empty(PAGE))
    return this
  }

  /** Shows the page [pageClass] in the region through an inline frame. */
  @JvmOverloads
  fun page(pageClass: Class<out Page>, parameters: PageParameters = PageParameters()): DmInclude {
    addOrReplace(InlineFrame(PAGE, pageClass, parameters), empty(CONTENT))
    return this
  }

  companion object {
    /** The id a content component must carry. */
    const val CONTENT = "content"

    /** The id of the inline frame. */
    const val PAGE = "page"
  }
}
