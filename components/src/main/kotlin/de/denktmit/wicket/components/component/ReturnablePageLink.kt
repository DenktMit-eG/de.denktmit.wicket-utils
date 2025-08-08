package de.denktmit.rechnomat.webui.components

import de.denktmit.wicket.components.component.DmPageLink
import org.apache.wicket.Page
import org.apache.wicket.model.IModel
import org.apache.wicket.request.mapper.parameter.PageParameters

class ReturnablePageLink<T : Page>(
  id: String,
  pageClass: Class<T>,
  parameters: PageParameters? = null,
  bodyModel: IModel<String>? = null,
) : DmPageLink<T>(
    id,
    pageClass,
    parameters,
    bodyModel,
  ) {
  init {
    pageParameters?.add("returnTo", "previous")
  }
}
