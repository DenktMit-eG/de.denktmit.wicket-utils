package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.model.IModel

class DmAjaxCheckbox(
  id: String,
  model: IModel<Boolean>,
  @Transient
  val init: DmAjaxCheckbox.() -> Unit = {},
) : AjaxCheckBox(id, model) {
  @Transient
  var atUpdate: (target: AjaxRequestTarget) -> Unit = {}

  override fun onUpdate(target: AjaxRequestTarget) {
    atUpdate(target)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-checkbox", "dm-id-$id")
    init()
  }
}
