package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.form.Button

class DmButton(
    id: String,
    @Transient
  var onSubmit: (() -> Unit)? = null,
    @Transient
  val init: DmButton.() -> Unit = {},
) : Button(id) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-action-button", "dm-id-$id")
    init()
  }

  override fun onSubmit() {
    onSubmit?.invoke()
  }

  operator fun Behavior.unaryPlus() {
    this@DmButton.add(this)
  }
}
