package de.loosetie.util.wicket.page

import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters

open class MyPage(
  parameters: PageParameters?,
) : WebPage(parameters) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@MyPage.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyPage.add(this)
  }
}
