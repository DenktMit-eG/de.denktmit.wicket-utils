package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0

class MyTextArea<T : String?>(
  id: String,
  model: IModel<T>,
  @Transient
  val init: MyTextArea<T>.() -> Unit = {},
) : TextArea<T>(id, model) {
  constructor(
    field: KMutableProperty0<T>,
    id: String = field.name,
    model: IModel<T> = modelOf(field),
    init: MyTextArea<T>.() -> Unit = {},
  ) : this(id, model, init)

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-input", "dm-input-text-area", "dm-id-$id")
    init()
  }

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun AbstractAjaxBehavior.unaryPlus() {
    this@MyTextArea.add(this)
  }

  @Transient
  var onChange: (T) -> Unit = {}

  override fun onModelChanged() {
    super.onModelChanged()
    onChange(model.`object`)
  }

  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }
}
