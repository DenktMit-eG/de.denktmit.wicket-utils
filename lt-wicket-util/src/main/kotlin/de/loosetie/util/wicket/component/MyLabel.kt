package de.loosetie.util.wicket.component

import de.loosetie.util.wicket.modelOf
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

open class MyLabel(
  id: String,
  model: IModel<String>,
  @Transient
  val init: MyLabel.() -> Unit = {},
) : Label(id, model) {
  constructor(
    field: KProperty0<String>,
    id: String = field.name,
    model: IModel<String> = modelOf(field),
    init: MyLabel.() -> Unit = {},
  ) : this(id, model, init)

  constructor(
    id: String,
    value: String,
    init: MyLabel.() -> Unit = {},
  ) : this(id, modelOf { value }, init)

  @Transient
  var addOnComponentTag: (ComponentTag) -> Unit = {}

  @Transient
  var visible: (() -> Boolean)? = null

  operator fun Behavior.unaryPlus() {
    this@MyLabel.add(this)
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
    addCssClass("lt-label", "lt-id-$id")
    init()
  }
}
