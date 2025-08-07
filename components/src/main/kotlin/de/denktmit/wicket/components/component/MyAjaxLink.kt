package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.model.IModel

class MyAjaxLink<T>(
  id: String,
  @Transient
  val click: ((target: AjaxRequestTarget) -> Unit),
  val bodyModel: IModel<String>? = null,
  @Transient
  val init: MyAjaxLink<T>.() -> Unit = {},
) : AjaxLink<T>(id) {
  override fun onClick(p0: AjaxRequestTarget) {
    click(p0)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-link", "dm-ajax-link", "dm-id-$id")
    bodyModel?.apply {
      body = this
    }
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

  operator fun Component.unaryPlus() {
    this@MyAjaxLink.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyAjaxLink.add(this)
  }
}
