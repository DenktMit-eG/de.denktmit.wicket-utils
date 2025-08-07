package de.denktmit.wicket.components.component

import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.WebComponent

open class DmComponent(
  id: String,
  @Transient
  val init: DmComponent.() -> Unit = {},
) : WebComponent(id) {
  operator fun Behavior.unaryPlus() {
    this@DmComponent.add(this)
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
