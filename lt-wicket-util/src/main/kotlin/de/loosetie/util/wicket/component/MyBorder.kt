package de.loosetie.util.wicket.component

import de.loosetie.util.wicket.classNameAsCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.border.Border

open class MyBorder(
  id: String,
  @Transient
  private val init: MyBorder.() -> Unit = {},
) : Border(id) {
  operator fun Component.unaryPlus() {
    this@MyBorder.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyBorder.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-border", "lt-id-$id", "lt-cmp-${classNameAsCssClass()}")
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
