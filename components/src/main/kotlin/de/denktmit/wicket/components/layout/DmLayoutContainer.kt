package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference

/**
 * Base of the layout containers that are one Every Layout primitive over the author's own markup (section C): a
 * `DmContainer` with the hooks, `visible` by rule, the base sheet and the container's own sheet contributed once per
 * page. The children stay in the page's markup; the container only arranges them.
 */
abstract class DmLayoutContainer
@JvmOverloads
constructor(
  id: String,
  model: IModel<*>? = null,
  init: DmContainer.() -> Unit = {},
) : DmContainer(id, model, init) {

  /** The container's own sheet; `null` when the base sheet's primitive is all it needs. */
  protected open fun styleSheet(): ResourceReference? = null

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    styleSheet()?.let { response.render(CssHeaderItem.forReference(it)) }
  }
}
