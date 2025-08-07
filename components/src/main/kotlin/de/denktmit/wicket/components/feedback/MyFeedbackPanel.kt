package de.denktmit.wicket.components.feedback

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.feedback.FeedbackMessage
import org.apache.wicket.feedback.IFeedbackMessageFilter
import org.apache.wicket.markup.html.panel.FeedbackPanel

open class MyFeedbackPanel(
  id: String,
  filter: IFeedbackMessageFilter? = null,
  @Transient
  val init: MyFeedbackPanel.() -> Unit = {},
) : FeedbackPanel(id, filter) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-feedback", "dm-id-$id")
    init()
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
