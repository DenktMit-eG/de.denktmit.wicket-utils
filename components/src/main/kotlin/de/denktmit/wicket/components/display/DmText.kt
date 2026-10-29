package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.util.string.Strings

/**
 * A value as text (requirement G.66): with a [format], multi-line (line breaks kept), pre-formatted (rendered as
 * `pre`), hidden when empty, and with hyperlinks in the text detected where asked. The text is always escaped; only
 * the detected links become markup.
 */
open class DmText
@JvmOverloads
constructor(
  id: String,
  model: IModel<*>,
  @Transient
  private val format: ((Any?) -> String)? = null,
  var multiLine: Boolean = false,
  var preformatted: Boolean = false,
  var hideWhenEmpty: Boolean = false,
  var linkify: Boolean = false,
) : WebComponent(id, model) {

  /** The text as shown, before escaping. */
  fun text(): String = format?.invoke(defaultModelObject) ?: (defaultModelObject?.toString() ?: "")

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onConfigure() {
    super.onConfigure()
    if (hideWhenEmpty) isVisible = text().isNotBlank()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    if (preformatted) tag.name = "pre"
    if (multiLine && !preformatted) tag.append("class", "dm-text-multi-line", " ")
  }

  override fun onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    var body: CharSequence = Strings.escapeMarkup(text())
    if (linkify) body = LINK.replace(body) { "<a href=\"${it.value}\" rel=\"noopener\">${it.value}</a>" }
    if (multiLine && !preformatted) body = body.toString().replace("\r\n", "\n").replace("\n", "<br>")
    replaceComponentTagBody(markupStream, openTag, body)
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-display")))
  }

  companion object {
    private val LINK = Regex("https?://[^\\s<>\"']+")
  }
}
