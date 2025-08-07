package de.denktmit.wicket.components.component

import org.apache.wicket.ajax.AjaxEventBehavior
import org.apache.wicket.ajax.AjaxRequestTarget

class DmAjaxEventBehavior(
    event: String,
    @Transient
  private val eventBehavior: ((target: AjaxRequestTarget) -> Unit),
    @Transient
  val init: DmAjaxEventBehavior.() -> Unit = {},
) : AjaxEventBehavior(event) {
  override fun onEvent(target: AjaxRequestTarget) {
    eventBehavior(target)
  }
}
