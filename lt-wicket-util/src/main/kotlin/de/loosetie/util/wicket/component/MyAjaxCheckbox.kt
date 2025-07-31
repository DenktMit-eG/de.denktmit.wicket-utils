package de.loosetie.util.wicket.component

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox
import org.apache.wicket.model.IModel

class MyAjaxCheckbox(
  id: String,
  model: IModel<Boolean>,
  @Transient
  val init: MyAjaxCheckbox.() -> Unit = {},
) : AjaxCheckBox(id, model) {
  @Transient
  var atUpdate: (target: AjaxRequestTarget) -> Unit = {}

  override fun onUpdate(target: AjaxRequestTarget) {
    atUpdate(target)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-checkbox", "lt-id-$id")
    init()
  }
}
