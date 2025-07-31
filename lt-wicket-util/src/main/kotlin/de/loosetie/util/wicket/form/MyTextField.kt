package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.behavior.CssClassRemover
import de.loosetie.util.wicket.component.addCssClass
import de.loosetie.util.wicket.modelOf
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

open class MyTextField<V : String?>(
  id: String,
  model: IModel<V?>? = null,
  val size: Int = 100,
  @Transient
  val init: MyTextField<V>.() -> Unit = {},
) : TextField<V>(id, model) {
  constructor(
    field: KMutableProperty0<V>,
    id: String = field.name,
    model: IModel<V?> = modelOf(field),
    size: Int = 100,
    init: MyTextField<V>.() -> Unit = {},
  ) : this(id, model, size, init)

  operator fun Behavior.unaryPlus() {
    this@MyTextField.add(this)
  }

  @Transient
  var visible: (() -> Boolean)? = null

  @Transient
  var componentTag: (ComponentTag?) -> Unit = {}

  override fun onInitialize() {
    super.onInitialize()
    if (!model.`object`.isNullOrBlank()) {
      addCssClass("filled")
    }
    addCssClass("lt-text", "lt-input", "lt-input-text", "lt-id-$id")
    add(AttributeModifier("size", size))
    init()
  }

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
    if (!model.`object`.isNullOrBlank()) {
      addCssClass("filled")
    } else {
      +CssClassRemover("filled")
    }
  }

  override fun onComponentTag(tag: ComponentTag?) {
    super.onComponentTag(tag)
    componentTag(tag)
  }
}
