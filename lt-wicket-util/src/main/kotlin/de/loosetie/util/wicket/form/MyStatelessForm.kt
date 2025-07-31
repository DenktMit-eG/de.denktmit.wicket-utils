package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.component.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.form.StatelessForm

class MyStatelessForm<T>(
  id: String,
  @Transient
  val init: MyStatelessForm<T>.() -> Unit,
) : StatelessForm<T>(id) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@MyStatelessForm.add(this)
  }

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@MyStatelessForm.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-stateless-form", "lt-id-$id")
    init()
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
