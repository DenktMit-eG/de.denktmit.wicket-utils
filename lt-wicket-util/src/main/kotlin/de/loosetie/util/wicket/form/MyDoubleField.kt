package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.behavior.CssClassRemover
import de.loosetie.util.wicket.component.addCssClass
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.model.IModel
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.util.convert.converter.DoubleConverter

open class MyDoubleField(
  id: String,
  model: IModel<Double>? = null,
  val size: Int = 100,
  @Transient
  val init: MyDoubleField.() -> Unit = {},
) : NumberTextField<Double>(id, model, Double::class.java) {
  operator fun Behavior.unaryPlus() {
    this@MyDoubleField.add(this)
  }

  @Transient
  var visible: (() -> Boolean)? = null

  @Transient
  var componentTag: (ComponentTag?) -> Unit = {}

  override fun onInitialize() {
    super.onInitialize()
    if (model.`object` != null) {
      addCssClass("filled")
    }
    addCssClass("lt-text", "lt-input", "lt-input-text", "lt-id-$id")
    add(AttributeModifier("size", size))
    add(AttributeModifier("step", 0.01))
    init()
  }

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
    if (model.`object` != null) {
      addCssClass("filled")
    } else {
      +CssClassRemover("filled")
    }
  }

  override fun onComponentTag(tag: ComponentTag?) {
    super.onComponentTag(tag)
    componentTag(tag)
  }

  override fun createConverter(type: Class<*>?): IConverter<*> = DoubleConverter()
}
