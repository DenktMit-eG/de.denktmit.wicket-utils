package de.denktmit.wicket.components.component

import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.panel.GenericPanel

open class DmGenericPanel<T>(
  id: String,
  @Transient
  val init: DmGenericPanel<T>.() -> Unit = {},
) : GenericPanel<T>(id) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@DmGenericPanel.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@DmGenericPanel.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }
}
