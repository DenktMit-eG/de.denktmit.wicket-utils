package de.denktmit.wicket.components.feedback

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.feedback.FeedbackMessage
import org.apache.wicket.feedback.IFeedbackMessageFilter
import org.apache.wicket.markup.html.panel.FeedbackPanel

/**
 * The messages of the page (requirement H.77): info, success, warning and error, each with its icon and a
 * dismiss control served by script, placed once per page by the base page or per form with a filter. Wicket's
 * `FeedbackPanel` with the toolbox's markup, sheet and hooks.
 */
open class DmFeedbackPanel
@JvmOverloads
constructor(
  id: String,
  filter: IFeedbackMessageFilter? = null,
  /** Let the user dismiss a message (script). */
  var dismissible: Boolean = true,
  @Transient
  val init: DmFeedbackPanel.() -> Unit = {},
) : FeedbackPanel(id, filter) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-feedback")
    widgetHooks()
    outputMarkupId = true
    init()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    if (dismissible) tag.put("data-dm-feedback", "")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-feedback")))
    if (dismissible) {
      response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
      response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-feedback")))
    }
  }

  override fun onConfigure() {
    super.onConfigure()
    isVisible = anyMessage()
  }

  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }

  override fun getCSSClass(message: FeedbackMessage): String {
    val level =
      when (message.level) {
        FeedbackMessage.UNDEFINED -> "alert-light"
        FeedbackMessage.INFO -> "alert-info"
        FeedbackMessage.WARNING -> "alert-warning"
        FeedbackMessage.SUCCESS -> "alert-success"
        FeedbackMessage.ERROR -> "alert-danger"
        else -> super.getCSSClass(message)
      }
    return "alert $level"
  }
}
