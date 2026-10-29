package de.denktmit.wicket.components.behavior

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior
import java.time.Duration

/**
 * Refreshes the region it sits on every [interval] while the region is visible (requirement I.85), Wicket's
 * self-updating timer; [onRefresh] runs before the refresh so the page can update what the region reads.
 */
class RefreshTimer
@JvmOverloads
constructor(
  interval: Duration,
  @Transient
  private val onRefresh: ((AjaxRequestTarget) -> Unit)? = null,
) : AjaxSelfUpdatingTimerBehavior(interval) {
  override fun onPostProcessTarget(target: AjaxRequestTarget) {
    onRefresh?.invoke(target)
    super.onPostProcessTarget(target)
  }
}
