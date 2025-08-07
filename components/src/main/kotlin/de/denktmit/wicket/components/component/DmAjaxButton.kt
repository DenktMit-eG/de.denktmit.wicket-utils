package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxButton
import org.apache.wicket.behavior.AbstractAjaxBehavior

class DmAjaxButton(
  id: String,
  @Transient
  val init: DmAjaxButton.() -> Unit = {},
) : AjaxButton(id) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-action", "dm-action-submit", "dm-id-$id")
    init()
  }

  @Transient
  var onSubmit: ((AjaxRequestTarget?) -> Unit)? = null

  override fun onSubmit(target: AjaxRequestTarget) {
    onSubmit?.invoke(target)
  }

  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@DmAjaxButton.add(this)
  }
}
