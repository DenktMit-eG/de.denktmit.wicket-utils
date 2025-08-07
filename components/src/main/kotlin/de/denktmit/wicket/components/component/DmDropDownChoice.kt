package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.choice.DmChoiceRenderer
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

open class DmDropDownChoice<T>(
  id: String,
  model: IModel<T?>,
  choices: IModel<List<T>>,
  renderer: IChoiceRenderer<T> = DmChoiceRenderer(),
  @Transient
  val init: DmDropDownChoice<T>.() -> Unit = {},
) : DropDownChoice<T>(id, model, choices, renderer) {
  @Transient
  var visible: (() -> Boolean)? = null

  @Transient
  var enable: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
    enable?.let { setEnabled(it()) }
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-dropdownChoiceRenderer", "dm-id-$id")
    init()
  }

  companion object {
    fun <T> of(
      field: KProperty0<T?>,
      choices: IModel<List<T>>,
      id: String = field.name,
      model: IModel<T?> = modelOf(field),
      renderer: IChoiceRenderer<T> = DmChoiceRenderer(),
      init: DmDropDownChoice<T>.() -> Unit = {},
    ) = DmDropDownChoice(id, model, choices, renderer, init)
  }
}
