package de.loosetie.util.wicket.component

import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel

/**
 * @param id the binding to the html template (wicket:id)
 * @param model if omitted the model of the parent is used
 * @param init make use of `MyContainer(...) { +SomeChild }` instead of 'MyContainer container = MyContainer(...); container.add(SomeChild); ...'
 */
open class MyContainer(
  id: String,
  model: IModel<*>? = null,
  @Transient
  open val init: MyContainer.() -> Unit = {},
) : WebMarkupContainer(id, model) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@MyContainer.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyContainer.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-container", "lt-id-$id")
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
