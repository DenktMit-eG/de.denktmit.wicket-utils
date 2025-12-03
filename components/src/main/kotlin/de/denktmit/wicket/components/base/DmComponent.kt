package de.denktmit.wicket.components.base

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.classNameAsCssClass
import de.denktmit.wicket.model.PageParams
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.WebComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.request.component.IRequestablePage

open class DmComponent(
  id: String,
  model: IModel<*>? = null,
  @Transient
  val init: DmComponent.() -> Unit = {}
) : WebComponent(id, model) {

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-id-$id", "dm-${classNameAsCssClass()}")
    init()
  }

  open var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

}


context (Component)
operator fun Behavior.unaryPlus() {
  this@Component.add(this)
}

fun Component.enableAjax() {
  outputMarkupId = true
  outputMarkupPlaceholderTag = true
}

fun <P : PageParams> Component.setResponsePage(
  pageClass: Class<out IRequestablePage>,
  params: P,
  block: (P.() -> Unit) = {}
) {
  setResponsePage(pageClass, params.apply(block).pp)
}
