package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.MarkupStream
import org.apache.wicket.markup.html.WebComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/**
 * An inline symbol from the toolbox's SVG sprite (requirement G.72): renders `<svg class="icon"><use href="…#name"/>`
 * on the author's `<svg wicket:id>` or `<span wicket:id>` tag. With a [label] the icon is announced (`role="img"`,
 * `aria-label`), without one it is decorative (`aria-hidden`). Symbol names: close, chevron-down/up/left/right,
 * menu, more, search, check, plus, minus, info, warning, error, success, star, star-outline, calendar, clock,
 * upload, download, drag, maximize, minimize, external, user, home, edit, delete, folder, file, settings, spinner,
 * link, bold, italic, list, play, empty.
 */
open class DmIcon
@JvmOverloads
constructor(
  id: String,
  name: IModel<String>,
  private val label: IModel<String>? = null,
) : WebComponent(id, name) {

  @JvmOverloads
  constructor(id: String, name: String, label: String? = null) : this(id, Model.of(name), label?.let { Model.of(it) })

  /** The symbol name in the sprite. */
  val name: String get() = defaultModelObjectAsString

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.name = "svg"
    tag.put("class", (tag.getAttribute("class")?.let { "$it " } ?: "") + "icon")
    tag.put("focusable", "false")
    val text = label?.`object`
    if (text.isNullOrBlank()) {
      tag.put("aria-hidden", "true")
    } else {
      tag.put("role", "img")
      tag.put("aria-label", text)
    }
  }

  override fun onComponentTagBody(markupStream: MarkupStream, openTag: ComponentTag) {
    val href = urlFor(DmResources.ICONS, null).toString() + "#" + name
    replaceComponentTagBody(markupStream, openTag, "<use href=\"$href\"></use>")
  }
}
