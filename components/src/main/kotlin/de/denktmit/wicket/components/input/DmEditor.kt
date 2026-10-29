package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * Formatted text with a tool bar whose output is sanitised HTML (requirement E.53). The form reads a plain
 * `textarea` holding the HTML source, which is the whole widget without script; script adds a tool bar (bold,
 * italic, lists, links, headings) and an editable area that writes back into the textarea. On the server every
 * submitted value passes [HtmlSanitizer], so what reaches the model is the allow-listed subset whatever the
 * browser sent. [rows] sizes the fallback textarea.
 */
open class DmEditor
@JvmOverloads
constructor(
  id: String,
  model: IModel<String?>,
  val rows: Int = 8,
  init: DmInputPanel<String?>.() -> Unit = {},
) : DmInputPanel<String?>(id, model, init) {

  /** The textarea with the HTML source. */
  val source: TextArea<String> = object : TextArea<String>("source", Model(model.`object`)) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("rows", rows.toString())
    }
  }

  override fun addComponents() {
    outputMarkupId = true
    +source
  }

  override fun onBeforeRender() {
    source.modelObject = modelObject
    super.onBeforeRender()
  }

  override fun labelTarget(): FormComponent<*> = source

  override fun convertInput() {
    convertedInput = HtmlSanitizer.sanitize(source.convertedInput)?.takeIf { it.isNotBlank() }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-editor", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-editor")

  override fun script(): ResourceReference = DmResources.js("dm-editor")
}
