package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.RadioGroup

open class DmRadioGroup<T>(
  id: String,
  @Transient
  val init: DmRadioGroup<T>.() -> Unit = {},
) : RadioGroup<T>(id) {
  @Transient
  var addOnComponentTag: (ComponentTag) -> Unit = {}

  @Transient
  var visible: (() -> Boolean)? = null

  operator fun Behavior.unaryPlus() {
    this@DmRadioGroup.add(this)
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    addOnComponentTag(tag)
  }

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { isVisible = it() }
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-radiogroup", "dm-id-$id")
    init()
  }
}
