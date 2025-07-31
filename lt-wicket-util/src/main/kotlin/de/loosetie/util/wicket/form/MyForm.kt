package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.component.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.IModel

open class MyForm<T>(
  id: String,
  model: IModel<T>? = null,
  @Transient
  val init: MyForm<T>.() -> Unit,
) : Form<T>(id, model) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@MyForm.add(this)
  }

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@MyForm.add(this)
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-form", "lt-id-$id")
    init()
  }

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

  @Transient
  var onSubmit: (() -> Unit)? = null

  override fun onSubmit() {
    onSubmit?.invoke()
  }

  @Transient
  var onError: (() -> Unit)? = null

  override fun onError() {
    onError?.invoke()
  }
}
