package de.loosetie.util.wicket.border

import de.loosetie.util.wicket.component.MyLabel
import de.loosetie.util.wicket.component.MyPanel
import de.loosetie.util.wicket.form.MyTextField
import de.loosetie.util.wicket.modelOf
import org.apache.wicket.AttributeModifier
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

class MyAutoCompleteDropdownPanel(
  id: String,
  val model: IModel<String?>?,
  val optionsUrl: String,
  val labelName: String,
  val placeholder: String = "",
  @Transient
  private val init: MyAutoCompleteDropdownPanel.() -> Unit = {},
) : MyPanel(id, model) {
  constructor(
    field: KMutableProperty0<String>,
    id: String = field.name,
    model: IModel<String?>? = modelOf(field),
    optionsUrl: String,
    labelName: String,
    placeholder: String = "",
    init: MyAutoCompleteDropdownPanel.() -> Unit = {},
  ) : this(id, model, optionsUrl, labelName, placeholder, init)

  override fun onInitialize() {
    super.onInitialize()
  }

  override fun addComponents() {
    +MyTextField("textFieldValue", model ?: modelOf { placeholder }) {
      +AttributeModifier("onfocus", "DM.autoComplete(this,'$optionsUrl')")
    }
    +MyLabel("labelValue", labelName)
  }
}
