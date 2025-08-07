package de.denktmit.wicket.components.page

import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters

open class DmPage(
  parameters: PageParameters?,
) : WebPage(parameters) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@DmPage.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@DmPage.add(this)
  }
}
