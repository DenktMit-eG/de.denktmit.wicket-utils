package de.denktmit.wicket.components.border

import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmPanel
import de.denktmit.wicket.components.form.DmTextField
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.AttributeModifier
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

class DmAutoCompleteDropdownPanel(
  id: String,
  val model: IModel<String?>?,
  val optionsUrl: String,
  val labelName: String,
  val placeholder: String = "",
  @Transient
  private val init: DmAutoCompleteDropdownPanel.() -> Unit = {},
) : DmPanel(id, model) {
  constructor(
    field: KMutableProperty0<String>,
    id: String = field.name,
    model: IModel<String?>? = modelOf(field),
    optionsUrl: String,
    labelName: String,
    placeholder: String = "",
    init: DmAutoCompleteDropdownPanel.() -> Unit = {},
  ) : this(id, model, optionsUrl, labelName, placeholder, init)

  override fun onInitialize() {
    super.onInitialize()
  }

  override fun addComponents() {
    +DmTextField("textFieldValue", model ?: modelOf { placeholder }) {
      +AttributeModifier("onfocus", "Dm.autoComplete(this,'$optionsUrl')")
    }
    +DmLabel("labelValue", labelName)
  }
}
