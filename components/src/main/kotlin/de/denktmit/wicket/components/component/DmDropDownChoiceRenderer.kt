package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

class DmDropDownChoiceRenderer<T>(
  id: String,
  model: IModel<T?>,
  choices: IModel<List<T>>,
  renderer: ChoiceRenderer<T>,
  @Transient
  val init: () -> Unit = {},
) : DropDownChoice<T>(id, model, choices, renderer) {
  constructor(
    field: KProperty0<T?>,
    choices: IModel<List<T>>,
    id: String = field.name,
    model: IModel<T?> = modelOf(field),
    renderer: ChoiceRenderer<T>,
    init: () -> Unit = {},
  ) : this(id, model, choices, renderer, init)

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-dropdownChoiceRenderer", "dm-id-$id")
    init()
  }
}
