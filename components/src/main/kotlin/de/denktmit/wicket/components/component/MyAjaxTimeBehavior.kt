package de.denktmit.wicket.components.component

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import java.time.Duration

class MyAjaxTimeBehavior(
  interval: Duration,
) : AbstractAjaxTimerBehavior(interval) {
  @Transient
  var onAction: ((AjaxRequestTarget?) -> Unit)? = null

  override fun onTimer(target: AjaxRequestTarget?) {
    onAction?.let { it(target) }
  }
}
