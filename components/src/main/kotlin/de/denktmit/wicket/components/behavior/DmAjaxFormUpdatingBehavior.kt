package de.denktmit.wicket.components.behavior

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior

class DmAjaxFormUpdatingBehavior(
  event: String = "change",
  @Transient
  private val onUpdate: ((target: AjaxRequestTarget?) -> Unit)? = null,
) : AjaxFormComponentUpdatingBehavior(event) {
  override fun onUpdate(target: AjaxRequestTarget?) {
    onUpdate?.invoke(target)
  }
}
