package de.loosetie.util.wicket.component

import org.apache.wicket.ajax.AjaxEventBehavior
import org.apache.wicket.ajax.AjaxRequestTarget

class MyAjaxEventBehavior(
  event: String,
  @Transient
  private val eventBehavior: ((target: AjaxRequestTarget) -> Unit),
  @Transient
  val init: MyAjaxEventBehavior.() -> Unit = {},
) : AjaxEventBehavior(event) {
  override fun onEvent(target: AjaxRequestTarget) {
    eventBehavior(target)
  }
}
