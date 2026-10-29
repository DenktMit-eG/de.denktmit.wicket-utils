package de.denktmit.wicket.components.input

/**
 * Reduces HTML to the formatting the editor produces (requirement E.53): a fixed allow-list of tags with no
 * attributes but a safe `href` on links; every other tag, every attribute, every script, style and comment goes.
 * Text keeps its entities; a stray `<` becomes `&lt;`. A strict allow-list, not an HTML parser: what the list
 * does not name cannot pass.
 */
object HtmlSanitizer {

  /** The tags that pass. */
  @JvmField
  val ALLOWED: Set<String> = setOf("p", "br", "strong", "b", "em", "i", "u", "s", "ul", "ol", "li", "h1", "h2", "h3", "h4", "h5", "h6", "blockquote", "code", "pre", "a")

  private val TAG = Regex("<(/?)([a-zA-Z][a-zA-Z0-9]*)((?:\\s+[^<>]*)?)(/?)>")
  private val DROP_WITH_CONTENT = Regex("(?is)<(script|style|iframe|object|embed|noscript)\\b[^>]*>.*?</\\1\\s*>")
  private val COMMENT = Regex("(?s)<!--.*?-->")
  private val HREF = Regex("(?i)href\\s*=\\s*(\"([^\"]*)\"|'([^']*)'|([^\\s>]+))")
  private val SAFE_HREF = Regex("(?i)^(https?://|mailto:|/|#)")

  /** The sanitised form of [html], `null` for `null`. */
  @JvmStatic
  fun sanitize(html: String?): String? {
    if (html == null) return null
    var text = COMMENT.replace(html, "")
    text = DROP_WITH_CONTENT.replace(text, "")
    val out = StringBuilder()
    var last = 0
    for (match in TAG.findAll(text)) {
      out.append(escapeStray(text.substring(last, match.range.first)))
      last = match.range.last + 1
      val closing = match.groupValues[1] == "/"
      val name = match.groupValues[2].lowercase()
      if (name !in ALLOWED) continue
      if (closing) {
        if (name != "br") out.append("</").append(name).append(">")
      } else if (name == "a") {
        val href = HREF.find(match.groupValues[3])?.let { it.groupValues[2].ifEmpty { it.groupValues[3].ifEmpty { it.groupValues[4] } } }?.trim()
        if (href != null && SAFE_HREF.containsMatchIn(href) && !href.contains('"')) out.append("<a href=\"").append(href).append("\" rel=\"noopener\">") else out.append("<a>")
      } else if (name == "br") {
        out.append("<br>")
      } else {
        out.append("<").append(name).append(">")
      }
    }
    out.append(escapeStray(text.substring(last)))
    return out.toString()
  }

  private fun escapeStray(text: String): String = text.replace("<", "&lt;")
}
