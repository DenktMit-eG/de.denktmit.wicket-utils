package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.Orientation
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** The two values of a [DmRangeSlider], low first. */
data class SliderRange(val low: Double, val high: Double) : Serializable

/**
 * Two values on a scale (requirement E.48): two native range inputs over the same track, kept in order on the
 * server (a crossed pair is swapped) and by script, the values shown in an `output`. Vertical where asked.
 */
open class DmRangeSlider
@JvmOverloads
constructor(
  id: String,
  model: IModel<SliderRange?>,
  val min: Double = 0.0,
  val max: Double = 100.0,
  val step: Double = 1.0,
  var orientation: Orientation = Orientation.HORIZONTAL,
  init: DmInputPanel<SliderRange?>.() -> Unit = {},
) : DmInputPanel<SliderRange?>(id, model, init) {

  private fun range(id: String, value: Double?) = object : NumberTextField<Double>(id, @Suppress("UNCHECKED_CAST") (Model(value) as IModel<Double>), Double::class.javaObjectType) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("type", "range")
      if (orientation == Orientation.VERTICAL) tag.put("orient", "vertical")
      tag.put("min", format(min))
      tag.put("max", format(max))
      tag.put("step", format(step))
    }

    override fun getInputTypes(): Array<String> = arrayOf("range")

    override fun getModelValue(): String = modelObject?.let { format(it) } ?: ""

    @Suppress("UNCHECKED_CAST")
    override fun <C : Any?> getConverter(type: Class<C>): org.apache.wicket.util.convert.IConverter<C> = SliderNumberConverter() as org.apache.wicket.util.convert.IConverter<C>
  }.apply { setMinimum(min); setMaximum(max); setStep(step) }

  /** The input of the lower value. */
  val low: NumberTextField<Double> = range("low", model.`object`?.low ?: min)

  /** The input of the upper value. */
  val high: NumberTextField<Double> = range("high", model.`object`?.high ?: max)

  override fun addComponents() {
    outputMarkupId = true
    +low
    +high
    +Label("value", object : IModel<String> {
      override fun getObject(): String = this@DmRangeSlider.modelObject?.let { "${format(it.low)} – ${format(it.high)}" } ?: ""
    })
  }

  /** A value as the slider shows it: without a trailing `.0` when it is whole. */
  fun format(value: Double): String = SliderNumberConverter().convertToString(value, null) ?: ""

  override fun onBeforeRender() {
    modelObject?.let { low.modelObject = it.low; high.modelObject = it.high }
    super.onBeforeRender()
  }

  override fun labelTarget(): FormComponent<*> = low

  override fun convertInput() {
    val a = low.convertedInput
    val b = high.convertedInput
    convertedInput = if (a == null || b == null) null else SliderRange(minOf(a, b), maxOf(a, b))
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-range-slider", "")
    tag.append("class", "dm-slider-${orientation.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-slider")

  override fun script(): ResourceReference = DmResources.js("dm-slider")
}
