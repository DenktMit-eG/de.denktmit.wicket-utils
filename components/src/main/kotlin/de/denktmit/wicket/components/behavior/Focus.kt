package de.denktmit.wicket.components.behavior

import org.apache.wicket.Component
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler

/**
 * Brings a widget into view and gives it focus after an action (requirement I.91): script appended to the Ajax
 * response. Without script the page simply renders, which is the no-script form of every Ajax action.
 */
object Focus {
  /** Scrolls [component] into view. */
  @JvmStatic
  @JvmOverloads
  fun scrollTo(target: IPartialPageRequestHandler, component: Component, smooth: Boolean = true) {
    component.outputMarkupId = true
    target.appendJavaScript("(function(e){if(e)e.scrollIntoView({behavior:'${if (smooth) "smooth" else "auto"}',block:'nearest'})})(document.getElementById('${component.markupId}'))")
  }

  /** Gives [component] the focus. */
  @JvmStatic
  fun focus(target: IPartialPageRequestHandler, component: Component) {
    component.outputMarkupId = true
    target.focusComponent(component)
  }
}

/** Scrolls [component] into view through this target. */
fun IPartialPageRequestHandler.scrollTo(component: Component) = Focus.scrollTo(this, component)

/** Gives [component] the focus through this target. */
fun IPartialPageRequestHandler.focus(component: Component) = Focus.focus(this, component)
