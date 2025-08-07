package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.behavior.CssClassRemover
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

open class DmTextField<V : String?>(
    id: String,
    model: IModel<V?>? = null,
    val size: Int = 100,
    @Transient
  val init: DmTextField<V>.() -> Unit = {},
) : TextField<V>(id, model) {
  constructor(
      field: KMutableProperty0<V>,
      id: String = field.name,
      model: IModel<V?> = modelOf(field),
      size: Int = 100,
      init: DmTextField<V>.() -> Unit = {},
  ) : this(id, model, size, init)

  operator fun Behavior.unaryPlus() {
    this@DmTextField.add(this)
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
    addCssClass("dm-text", "dm-input", "dm-input-text", "dm-id-$id")
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
