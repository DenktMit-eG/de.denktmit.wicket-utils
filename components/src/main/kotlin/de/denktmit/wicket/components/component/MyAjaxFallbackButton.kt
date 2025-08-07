package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.form.Form
import java.util.*

class MyAjaxFallbackButton(
  id: String,
  form: Form<*>,
  @Transient
  val init: MyAjaxFallbackButton.() -> Unit = {},
) : AjaxFallbackButton(id, form) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-action", "dm-action-submit", "dm-id-$id")
    init()
  }

  @Transient
  var execOnError: ((AjaxRequestTarget?) -> Unit)? = null

  override fun onError(target: Optional<AjaxRequestTarget>) {
    execOnError?.invoke(target.orElse(null))
  }

  @Transient
  var onSubmit: ((AjaxRequestTarget?) -> Unit)? = null

  override fun onSubmit(target: Optional<AjaxRequestTarget>) {
    onSubmit?.invoke(target.orElse(null))
  }

  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@MyAjaxFallbackButton.add(this)
  }

  operator fun Component.unaryPlus() {
    this@MyAjaxFallbackButton.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyAjaxFallbackButton.add(this)
  }

  @Transient
  var visible: (() -> Boolean)? = null
}
