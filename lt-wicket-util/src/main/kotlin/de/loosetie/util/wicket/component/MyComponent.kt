package de.loosetie.util.wicket.component

import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.WebComponent

open class MyComponent(
  id: String,
  @Transient
  val init: MyComponent.() -> Unit = {},
) : WebComponent(id) {
  operator fun Behavior.unaryPlus() {
    this@MyComponent.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
//    addCssClass("lt-component", "lt-id-$id")
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }
}
