package de.loosetie.util.wicket.component

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.behavior.AbstractAjaxBehavior

class MyAjaxButton(
  id: String,
  @Transient
  val init: MyAjaxButton.() -> Unit = {},
) : AjaxButton(id) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-action", "lt-action-submit", "lt-id-$id")
    init()
  }

  @Transient
  var onSubmit: ((AjaxRequestTarget?) -> Unit)? = null

  override fun onSubmit(target: AjaxRequestTarget) {
    onSubmit?.invoke(target)
  }

  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@MyAjaxButton.add(this)
  }
}
