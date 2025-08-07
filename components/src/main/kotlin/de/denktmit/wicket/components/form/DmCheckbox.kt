package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

class DmCheckbox(
    id: String,
    model: IModel<Boolean>,
    @Transient
  val init: DmCheckbox.() -> Unit = {},
) : CheckBox(id, model) {
  constructor(
      field: KMutableProperty0<Boolean>,
      id: String = field.name,
      model: IModel<Boolean> = modelOf(field),
      init: DmCheckbox.() -> Unit = {},
  ) : this(id, model, init)

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-checkbox", "dm-id-$id")
    init()
  }
}
