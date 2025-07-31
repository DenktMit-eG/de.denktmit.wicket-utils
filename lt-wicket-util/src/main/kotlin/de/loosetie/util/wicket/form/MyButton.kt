package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.component.addCssClass
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.form.Button

class MyButton(
  id: String,
  @Transient
  var onSubmit: (() -> Unit)? = null,
  @Transient
  val init: MyButton.() -> Unit = {},
) : Button(id) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-action-button", "lt-id-$id")
    init()
  }

  override fun onSubmit() {
    onSubmit?.invoke()
  }

  operator fun Behavior.unaryPlus() {
    this@MyButton.add(this)
  }
}
