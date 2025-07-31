package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.component.addCssClass
import de.loosetie.util.wicket.modelOf
import org.apache.wicket.extensions.markup.html.form.datetime.LocalDateTextField
import org.apache.wicket.model.IModel
import java.time.LocalDate
import kotlin.reflect.KProperty0

class MyLocalDateField(
  id: String,
  model: IModel<LocalDate?>,
  @Transient
  val init: MyLocalDateField.() -> Unit = {},
) : LocalDateTextField(id, model, "dd.MM.yyyy") {
  constructor(
    field: KProperty0<LocalDate?>,
    id: String = field.name,
    model: IModel<LocalDate?> = modelOf(field),
    init: MyLocalDateField.() -> Unit = {},
  ) : this(id, model, init)

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-input", "lt-input-local-date", "lt-id-$id")
    init()
  }
}
