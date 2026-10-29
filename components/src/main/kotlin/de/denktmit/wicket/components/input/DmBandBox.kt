package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.HiddenField
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A text whose popup is any panel the author builds (requirement E.44): a search form, a table, a tree. The popup
 * is a `details` element that opens without script; the panel calls [choose] with the picked value, which fills the
 * read-only display and the hidden key the form submits. [key] and [fromKey] map the value to the text the form
 * carries and back; [display] is what the user sees.
 */
open class DmBandBox<T : java.io.Serializable>
@JvmOverloads
constructor(
  id: String,
  model: IModel<T?>,
  @Transient
  private val display: (T) -> String,
  @Transient
  private val key: (T) -> String,
  @Transient
  private val fromKey: (String) -> T?,
  @Transient
  private val popup: (String) -> Component,
  val placeholder: IModel<String> = Model.of("Choose"),
  init: DmInputPanel<T?>.() -> Unit = {},
) : DmInputPanel<T?>(id, model, init) {

  private val keyField = HiddenField("key", Model(model.`object`?.let(key) ?: ""))
  private val displayField = TextField("display", Model(model.`object`?.let(display) ?: ""))
  private val details = WebMarkupContainer("popup")

  /** Whether the popup is open on the next render. */
  var isOpen: Boolean = false

  override fun addComponents() {
    outputMarkupId = true
    +keyField
    +displayField.apply { isEnabled = false; add(org.apache.wicket.AttributeModifier.replace("placeholder", placeholder)) }
    +details.apply {
      add(DmIcon("icon", "chevron-down"))
      add(popup(CONTENT).also { require(it.id == CONTENT) { "popup id must be '$CONTENT'" } })
      add(object : org.apache.wicket.behavior.Behavior() {
        override fun onComponentTag(component: Component, tag: ComponentTag) {
          if (isOpen) tag.put("open", "open") else tag.remove("open")
        }
      })
    }
  }

  /** Takes [value] as the chosen one, closes the popup and, with a [target], refreshes the band box. */
  @JvmOverloads
  fun choose(value: T?, target: AjaxRequestTarget? = null) {
    modelObject = value
    keyField.modelObject = value?.let(key) ?: ""
    displayField.modelObject = value?.let(display) ?: ""
    isOpen = false
    target?.add(this)
  }

  override fun onBeforeRender() {
    keyField.modelObject = modelObject?.let(key) ?: ""
    displayField.modelObject = modelObject?.let(display) ?: ""
    super.onBeforeRender()
  }

  override fun labelTarget(): FormComponent<*> = displayField

  override fun convertInput() {
    convertedInput = keyField.convertedInput?.takeIf { it.isNotBlank() }?.let(fromKey)
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-band-box", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-band-box")

  override fun script(): ResourceReference = DmResources.js("dm-band-box")

  companion object {
    /** The id the popup panel must carry. */
    const val CONTENT = "content"
  }
}
