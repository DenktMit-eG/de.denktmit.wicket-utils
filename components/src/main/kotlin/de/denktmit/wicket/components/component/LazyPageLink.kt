package de.denktmit.wicket.components.component

import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.model.IModel
import org.apache.wicket.request.component.IRequestablePage
import org.apache.wicket.request.mapper.parameter.PageParameters

class LazyPageLink(
  id: String,
  model: IModel<String>? = null,
  @Transient
  val pageClass: () -> Class<out IRequestablePage>,
  @Transient
  val parameters: () -> PageParameters,
) : Link<String>(id, model) {
  override fun onClick() {
    setResponsePage(pageClass(), parameters())
  }
}
