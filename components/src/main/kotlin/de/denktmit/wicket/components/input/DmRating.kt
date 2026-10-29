package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.Radio
import org.apache.wicket.markup.html.form.RadioGroup
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A value as a row of symbols (requirement E.49): one radio per step, styled as a star, so the rating submits and
 * is keyboard-reachable without script; [halfSteps] doubles the radios; [readOnly] renders the symbols only, with
 * the value announced.
 */
open class DmRating
@JvmOverloads
constructor(
  id: String,
  model: IModel<Double?>,
  /** The number of symbols. */
  val symbols: Int = 5,
  val halfSteps: Boolean = false,
  var readOnly: Boolean = false,
  /** The accessible name of the group. */
  val groupLabel: IModel<String> = Model.of("Rating"),
  init: DmInputPanel<Double?>.() -> Unit = {},
) : DmInputPanel<Double?>(id, model, init) {

  /** The selectable values, ascending. */
  val values: List<Double> = (1..(if (halfSteps) symbols * 2 else symbols)).map { if (halfSteps) it / 2.0 else it.toDouble() }

  private val group = RadioGroup<Double>("group", model)

  override fun addComponents() {
    +group.apply {
      add(object : ListView<Double>("steps", values) {
        override fun populateItem(item: ListItem<Double>) {
          val value = item.modelObject
          item.add(Radio("radio", Model.of(value), group))
          item.add(WebMarkupContainer("symbol").apply {
            add(DmIcon("icon", if (value % 1.0 == 0.0) "star" else "star-outline"))
            add(org.apache.wicket.AttributeModifier.replace("title", format(value)))
          })
          item.add(object : org.apache.wicket.behavior.Behavior() {
            override fun onComponentTag(component: org.apache.wicket.Component, tag: ComponentTag) {
              if (value % 1.0 != 0.0) tag.append("class", "dm-rating-half", " ")
              val current = this@DmRating.modelObject ?: 0.0
              if (value <= current) tag.append("class", "dm-rating-lit", " ")
            }
          })
        }
      })
    }
  }

  /** A value as the rating announces it. */
  fun format(value: Double): String = (if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()) + " of " + symbols

  override fun labelTarget(): FormComponent<*>? = null

  override fun convertInput() {
    convertedInput = if (readOnly) modelObject else group.convertedInput
  }

  override fun onConfigure() {
    super.onConfigure()
    group.isEnabled = !readOnly
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", if (readOnly) "img" else "radiogroup")
    tag.put("aria-label", groupLabel.`object` + (if (readOnly) ": " + (modelObject?.let { format(it) } ?: "none") else ""))
    if (readOnly) tag.append("class", "dm-rating-read-only", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-rating")
}
