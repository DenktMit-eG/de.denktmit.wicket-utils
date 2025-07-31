package de.loosetie.util.wicket.component

import de.loosetie.util.wicket.classNameAsCssClass
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
    addCssClass("lt-panel", "lt-id-$id", "lt-cmp-${classNameAsCssClass()}")
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
