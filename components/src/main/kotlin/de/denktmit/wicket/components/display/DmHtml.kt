package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.input.HtmlSanitizer
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.html.WebComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.util.string.Strings

/** How [DmHtml] treats its markup. */
enum class HtmlTrust { TRUSTED, SANITIZED, ESCAPED }

/**
 * Markup inserted as it is when [trust] is `TRUSTED` (requirement G.68), reduced to the editor's allow-list when
 * `SANITIZED`, and escaped to visible text when `ESCAPED`. The default is `SANITIZED`: trusted markup is a
 * decision the author takes on purpose.
 */
open class DmHtml
@JvmOverloads
constructor(
  id: String,
  model: IModel<String?>,
  var trust: HtmlTrust = HtmlTrust.SANITIZED,
) : WebComponent(id, model) {

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    val html = defaultModelObject?.toString() ?: ""
    val body: CharSequence = when (trust) {
      HtmlTrust.TRUSTED -> html
      HtmlTrust.SANITIZED -> HtmlSanitizer.sanitize(html) ?: ""
      HtmlTrust.ESCAPED -> Strings.escapeMarkup(html)
    }
    replaceComponentTagBody(markupStream, openTag, body)
  }
}
