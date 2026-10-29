package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.Orientation
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.NumberTextField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** A tick mark on a slider: the value and, optionally, what the page shows there. */
data class SliderMark
@JvmOverloads
constructor(val value: Double, val label: String? = null) : Serializable

/**
 * One value on a scale (requirement E.48): a native `input type="range"` with minimum, maximum and step, the value
 * shown in an `output` element that script keeps current, tick [marks] as a `datalist`, vertical where asked. The
 * input submits without script; only the live value display needs it.
 */
open class DmSlider
@JvmOverloads
constructor(
  id: String,
  model: IModel<Double?>,
  val min: Double = 0.0,
  val max: Double = 100.0,
  val step: Double = 1.0,
  val marks: List<SliderMark> = emptyList(),
  var orientation: Orientation = Orientation.HORIZONTAL,
  /** Show the current value next to the slider. */
  var showValue: Boolean = true,
  init: DmInputPanel<Double?>.() -> Unit = {},
) : DmInputPanel<Double?>(id, model, init) {

  /** The native range input. */
  val input: NumberTextField<Double> = object : NumberTextField<Double>("input", @Suppress("UNCHECKED_CAST") (model as IModel<Double>), Double::class.javaObjectType) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("type", "range")
      if (marks.isNotEmpty()) tag.put("list", this@DmSlider.markupId + "-marks")
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

  override fun addComponents() {
    outputMarkupId = true
    +input
    +Label("value", object : org.apache.wicket.model.IModel<String> {
      override fun getObject(): String = this@DmSlider.modelObject?.let { format(it) } ?: ""
    }).apply { outputMarkupId = true; add(org.apache.wicket.AttributeModifier.replace("for", input.markupId)) }.apply { isVisible = showValue }
    +WebMarkupContainer("marks").apply {
      isVisible = marks.isNotEmpty()
      setMarkupId(this@DmSlider.markupId + "-marks")
      add(object : ListView<SliderMark>("mark", marks) {
        override fun populateItem(item: ListItem<SliderMark>) {
          item.add(org.apache.wicket.AttributeModifier.replace("value", format(item.modelObject.value)))
          item.modelObject.label?.let { item.add(org.apache.wicket.AttributeModifier.replace("label", it)) }
        }
      })
    }
  }

  /** A value as the slider shows it: without a trailing `.0` when it is whole. */
  fun format(value: Double): String = SliderNumberConverter().convertToString(value, null) ?: ""

  override fun labelTarget(): FormComponent<*> = input

  override fun convertInput() {
    convertedInput = input.convertedInput
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-slider", "")
    tag.append("class", "dm-slider-${orientation.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-slider")

  override fun script(): ResourceReference = DmResources.js("dm-slider")
}

/** Shows whole numbers without a trailing `.0` and parses any decimal, for the range inputs. */
internal class SliderNumberConverter : org.apache.wicket.util.convert.IConverter<Double> {
  override fun convertToObject(value: String?, locale: java.util.Locale?): Double? = value?.trim()?.takeIf { it.isNotEmpty() }?.let {
    it.toDoubleOrNull() ?: throw org.apache.wicket.util.convert.ConversionException("not a number: $it").setSourceValue(it).setTargetType(Double::class.java)
  }

  override fun convertToString(value: Double?, locale: java.util.Locale?): String? = value?.let { if (it == Math.floor(it) && !it.isInfinite()) it.toLong().toString() else it.toString() }
}
