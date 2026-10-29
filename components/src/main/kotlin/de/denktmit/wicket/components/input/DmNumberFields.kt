package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.model.IModel

/**
 * A `long` input with minimum, maximum and step as the browser's own constraints and as server-side validation
 * (requirement E.40); the model may hold `null` for "not set".
 */
open class DmLongField
@JvmOverloads
constructor(
  id: String,
  model: IModel<Long?>? = null,
  min: Long? = null,
  max: Long? = null,
  step: Long? = null,
  init: DmLongField.() -> Unit = {},
) : NumberTextField<Long>(id, @Suppress("UNCHECKED_CAST") (model as IModel<Long>?), Long::class.javaObjectType) {

  @Transient
  private val setup = init

  init {
    min?.let { setMinimum(it) }
    max?.let { setMaximum(it) }
    step?.let { setStep(it) }
  }

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    setup()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("inputmode", "numeric")
  }
}

/**
 * A number input of any boxed numeric [type] with minimum, maximum and step (requirement E.40): the general form
 * of the integer, long, decimal and double fields. [DmSpinner] adds the up and down controls.
 */
open class DmNumberField<N>
@JvmOverloads
constructor(
  id: String,
  model: IModel<N?>?,
  type: Class<N>,
  min: N? = null,
  max: N? = null,
  step: N? = null,
  init: DmNumberField<N>.() -> Unit = {},
) : NumberTextField<N>(id, @Suppress("UNCHECKED_CAST") (model as IModel<N>?), type) where N : Number, N : Comparable<N> {

  @Transient
  private val setup = init

  init {
    min?.let { setMinimum(it) }
    max?.let { setMaximum(it) }
    step?.let { setStep(it) }
  }

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    setup()
  }
}
