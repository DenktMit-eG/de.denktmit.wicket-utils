package de.denktmit.wicket.components.component

import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.model.IModel
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

class MonthChoice(
  id: String,
  model: IModel<Int>,
) : DropDownChoice<Int>(
    id,
    model,
    Month.entries.map { it.value },
    { Month.of(it).getDisplayName(TextStyle.FULL, Locale.GERMAN) },
  )
