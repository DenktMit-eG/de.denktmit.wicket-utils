package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.classNameAsCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel

abstract class MyPanel(
  id: String,
  model: IModel<*>? = null,
  @Transient
  private val init: MyPanel.() -> Unit = {},
) : Panel(id, model) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@MyPanel.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyPanel.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-panel", "dm-id-$id", "dm-cmp-${classNameAsCssClass()}")
    addComponents()
    init()
  }

  open fun addComponents() {
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
