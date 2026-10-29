package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.layout.DmWindow
import de.denktmit.wicket.components.layout.WindowMode
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import java.util.Optional

/** The answers a [DmMessageBox] offers. */
enum class Answer { OK, CANCEL, YES, NO, CUSTOM }

/** One button of a [DmMessageBox]: its label and the answer it gives. */
data class MessageButton
@JvmOverloads
constructor(val label: String, val answer: Answer = Answer.CUSTOM, val primary: Boolean = false) : java.io.Serializable

/**
 * A modal question with buttons (requirement H.79) that returns the answer to the page: a [DmWindow] in modal mode
 * whose content is the question and whose footer holds the buttons; [onAnswer] receives the answer and the
 * button's label. [okCancel] and [yesNo] build the usual pairs.
 */
open class DmMessageBox
@JvmOverloads
constructor(
  id: String,
  title: IModel<String>,
  val question: IModel<String>,
  val buttons: List<MessageButton> = okCancel(),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, title, init) {

  @JvmOverloads
  constructor(id: String, title: String, question: String, buttons: List<MessageButton> = okCancel(), init: DmWidgetPanel.() -> Unit = {}) :
    this(id, Model.of(title), Model.of(question), buttons, init)

  /** Called with the answer and the label of the button pressed; closing the window counts as `CANCEL`. */
  @Transient
  var onAnswer: ((Answer, String, AjaxRequestTarget?) -> Unit)? = null

  /** The last answer given, `null` before the first. */
  var answer: Answer? = null
    private set

  private lateinit var window: DmWindow<String>

  /** Opens the box; with a [target] in place. */
  @JvmOverloads
  fun open(target: AjaxRequestTarget? = null) = window.open(target)

  /** Whether the box is open. */
  val isOpen: Boolean get() = window.isOpen

  override fun addComponents() {
    outputMarkupId = true
    @Suppress("UNCHECKED_CAST")
    window = DmWindow<String>(WINDOW, defaultModel as IModel<String>, WindowMode.MODAL, QuestionPanel(DmWindow.CONTENT, question)).apply {
      draggable = false
      resizable = false
      onClose = { result -> if (result == null) { answer = Answer.CANCEL; onAnswer?.invoke(Answer.CANCEL, "", null) } }
    }
    buttons.forEach { button ->
      window.action { id ->
        object : AjaxFallbackLink<Void>(id) {
          override fun onClick(target: Optional<AjaxRequestTarget>) {
            answer = button.answer
            window.onClose = null
            window.close(target.orElse(null), button.label)
            onAnswer?.invoke(button.answer, button.label, target.orElse(null))
            window.onClose = { result -> if (result == null) { answer = Answer.CANCEL; onAnswer?.invoke(Answer.CANCEL, "", null) } }
          }
        }.apply {
          setBody(Model.of(button.label))
          add(org.apache.wicket.AttributeModifier.append("class", if (button.primary) "dm-message-button dm-message-primary" else "dm-message-button"))
        }
      }
    }
    +window
  }

  /** The question text inside the window. */
  class QuestionPanel(id: String, question: IModel<String>) : Panel(id) {
    init {
      add(Label("question", question))
    }
  }

  companion object {
    /** The id of the inner window. */
    const val WINDOW = "window"

    @JvmStatic
    fun okCancel(): List<MessageButton> = listOf(MessageButton("OK", Answer.OK, primary = true), MessageButton("Cancel", Answer.CANCEL))

    @JvmStatic
    fun yesNo(): List<MessageButton> = listOf(MessageButton("Yes", Answer.YES, primary = true), MessageButton("No", Answer.NO))
  }
}
