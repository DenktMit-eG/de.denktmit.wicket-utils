package de.denktmit.wicket.components.component

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
//    addCssClass("dm-component", "dm-id-$id")
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }
}
