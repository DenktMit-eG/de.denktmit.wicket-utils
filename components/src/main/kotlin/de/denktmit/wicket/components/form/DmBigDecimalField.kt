package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.behavior.CssClassRemover
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.model.IModel
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.util.convert.converter.BigDecimalConverter
import java.math.BigDecimal

open class DmBigDecimalField(
    id: String,
    model: IModel<BigDecimal?>? = null,
    val size: Int = 100,
    @Transient
  val init: DmBigDecimalField.() -> Unit = {},
) : NumberTextField<BigDecimal>(id, model, BigDecimal::class.java) {
  operator fun Behavior.unaryPlus() {
    this@DmBigDecimalField.add(this)
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
    addCssClass("dm-text", "dm-input", "dm-input-text", "dm-id-$id")
    add(AttributeModifier("size", size))
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

  override fun createConverter(type: Class<*>?): IConverter<*> = BigDecimalConverter()
}
