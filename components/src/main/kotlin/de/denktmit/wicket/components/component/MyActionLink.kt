package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.model.IModel

class MyActionLink(
  id: String,
  val bodyModel: IModel<String>? = null,
  @Transient
  val init: MyActionLink.() -> Unit = {},
) : Link<Any>(id) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-link", "dm-action-link", "dm-id-$id")
    body = bodyModel
    init()
  }

  @Transient
  var onClick: (() -> Unit)? = null

  override fun onClick() {
    onClick?.invoke()
  }

  operator fun Component.unaryPlus() {
    this@MyActionLink.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyActionLink.add(this)
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }
}
