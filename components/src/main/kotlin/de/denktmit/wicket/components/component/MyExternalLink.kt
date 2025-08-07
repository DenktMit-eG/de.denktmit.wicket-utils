package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.link.ExternalLink
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

class MyExternalLink(
  id: String,
  href: IModel<String?>,
  label: String? = null,
  @Transient
  val init: MyExternalLink.() -> Unit? = {},
) : ExternalLink(id, href, modelOf { label }) {
  constructor(
    id: String,
    href: String?,
    label: String? = null,
    init: MyExternalLink.() -> Unit? = {},
  ) : this(id, modelOf { href }, label, init)

  constructor(
    prop: KProperty0<String?>,
    label: String? = prop.get(),
    id: String = prop.name,
    href: String? = prop.get(),
  ) : this(id, modelOf { href }, label)

  operator fun Behavior.unaryPlus() {
    this@MyExternalLink.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-external-link lt-link-$id")
    init()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("target", "_blank")
  }
}
