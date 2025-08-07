package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.classNameAsCssClass
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
    addCssClass("dm-border", "dm-id-$id", "dm-cmp-${classNameAsCssClass()}")
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
