package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.form.StatelessForm

class DmStatelessForm<T>(
    id: String,
    @Transient
  val init: DmStatelessForm<T>.() -> Unit,
) : StatelessForm<T>(id) {
  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@DmStatelessForm.add(this)
  }

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@DmStatelessForm.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-stateless-form", "dm-id-$id")
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
