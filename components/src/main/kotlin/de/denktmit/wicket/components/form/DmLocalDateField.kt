package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField
import org.apache.wicket.model.IModel
import java.time.LocalDate
import kotlin.reflect.KProperty0

class DmLocalDateField(
    id: String,
    model: IModel<LocalDate?>,
    @Transient
  val init: DmLocalDateField.() -> Unit = {},
) : LocalDateTextField(id, model, "dd.MM.yyyy") {
  constructor(
      field: KProperty0<LocalDate?>,
      id: String = field.name,
      model: IModel<LocalDate?> = modelOf(field),
      init: DmLocalDateField.() -> Unit = {},
  ) : this(id, model, init)

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-input", "dm-input-local-date", "dm-id-$id")
    init()
  }
}
