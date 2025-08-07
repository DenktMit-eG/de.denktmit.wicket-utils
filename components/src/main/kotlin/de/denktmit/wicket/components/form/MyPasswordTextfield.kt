package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.form.PasswordTextField
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

class MyPasswordTextfield(
  id: String,
  model: IModel<String?>? = null,
  val size: Int = 100,
  @Transient
  val init: MyPasswordTextfield.() -> Unit = {},
) : PasswordTextField(id, model) {
  constructor(
    field: KMutableProperty0<String?>,
    id: String = field.name,
    model: IModel<String?> = modelOf(field),
    size: Int = 100,
    init: MyPasswordTextfield.() -> Unit = {},
  ) : this(id, model, size, init)

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-input", "dm-input-password", "dm-id-$id")
    add(AttributeModifier("size", size))
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

  operator fun Behavior.unaryPlus() {
    this@MyPasswordTextfield.add(this)
  }
}
