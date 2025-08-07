package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

class MyNullableLabel(
  id: String,
  model: IModel<String?>,
  @Transient
  val init: MyNullableLabel.() -> Unit = {},
) : Label(id, model) {
  constructor(
    field: KProperty0<String?>,
    id: String = field.name,
    model: IModel<String?> = modelOf(field),
    init: MyNullableLabel.() -> Unit = {},
  ) : this(id, model, init)

  constructor(
    id: String,
    value: String?,
    init: MyNullableLabel.() -> Unit = {},
  ) : this(id, modelOf { value }, init)

  @Transient
  var addOnComponentTag: (ComponentTag) -> Unit = {}

  @Transient
  var visible: (() -> Boolean)? = null

  operator fun Behavior.unaryPlus() {
    this@MyNullableLabel.add(this)
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
    addCssClass("dm-label", "dm-id-$id")
    init()
  }
}
