package de.denktmit.wicket.components.base

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.classNameAsCssClass
import org.apache.wicket.Component
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel

/**
 * @param id the binding to the html template (wicket:id)
 * @param model if omitted the model of the parent is used
 * @param init make use of `DmContainer(...) { +SomeChild }` instead of 'DmContainer container = DmContainer(...); container.add(SomeChild); ...'
 */
open class DmContainer(
  id: String,
  model: IModel<*>? = null,
  @Transient
  open val init: DmContainer.() -> Unit = {}
) : WebMarkupContainer(id, model) {

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-id-$id", "dm-${classNameAsCssClass()}")
    init()
  }

  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

}


/** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
context (MarkupContainer)
operator fun Component.unaryPlus() {
  this@MarkupContainer.add(this@Component)
}
