package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.component.addCssClass
import de.loosetie.util.wicket.modelOf
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

class MyCheckbox(
  id: String,
  model: IModel<Boolean>,
  @Transient
  val init: MyCheckbox.() -> Unit = {},
) : CheckBox(id, model) {
  constructor(
    field: KMutableProperty0<Boolean>,
    id: String = field.name,
    model: IModel<Boolean> = modelOf(field),
    init: MyCheckbox.() -> Unit = {},
  ) : this(id, model, init)

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-checkbox", "lt-id-$id")
    init()
  }
}
