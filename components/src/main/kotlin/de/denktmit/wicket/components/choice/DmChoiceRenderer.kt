package de.denktmit.wicket.components.choice

import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.model.IModel

class DmChoiceRenderer<T>(
  @Transient
  val displayValue: (T) -> String = { v ->
    "$v"
  },
  @Transient
  val idValue: (T, Int) -> String = { _, i ->
    "$i"
  },
) : IChoiceRenderer<T> {
  override fun getDisplayValue(`object`: T): Any = displayValue(`object`)

  override fun getIdValue(
    `object`: T,
    index: Int,
  ): String = idValue(`object`, index)

  override fun getObject(
    id: String,
    choices: IModel<out List<T>>,
  ): T? {
    if (id.isBlank()) {
      return null
    }

    var i = 0
    return choices.`object`.find {
      id == idValue(it, i++)
    } ?: throw IllegalArgumentException("no value for id $id found")
  }
}
