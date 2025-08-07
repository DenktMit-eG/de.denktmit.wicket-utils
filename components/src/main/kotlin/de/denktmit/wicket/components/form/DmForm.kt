package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.IModel

open class DmForm<T>(
    id: String,
    model: IModel<T>? = null,
    @Transient
  val init: DmForm<T>.() -> Unit,
) : Form<T>(id, model) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@DmForm.add(this)
  }

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@DmForm.add(this)
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-form", "dm-id-$id")
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
