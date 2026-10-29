package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.MetaDataKey
import org.apache.wicket.Session
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import org.apache.wicket.validation.ValidationError
import java.security.SecureRandom

/**
 * A challenge the user has to pass before a form submits (requirement E.56): the toolbox's simple self-made
 * challenge, a small sum in words, with the answer kept in the session so that a stateless page can check it. The
 * model becomes `true` when the answer was right. An application plugs a stronger challenge in by subclassing
 * [newChallenge] and [check]; the slot stays the same.
 */
open class DmCaptcha
@JvmOverloads
constructor(
  id: String,
  model: IModel<Boolean> = Model.of(false),
  init: DmInputPanel<Boolean>.() -> Unit = {},
) : DmInputPanel<Boolean>(id, model, init) {

  /** A challenge: the question shown and the answer expected. */
  data class Challenge(val question: String, val answer: String) : java.io.Serializable

  private val answer: TextField<String> = TextField("answer", Model(""))

  /** Creates a fresh challenge; the default is a sum of two numbers below ten. */
  protected open fun newChallenge(): Challenge {
    val a = RANDOM.nextInt(9) + 1
    val b = RANDOM.nextInt(9) + 1
    return Challenge("What is $a + $b?", (a + b).toString())
  }

  /** Whether [given] answers [challenge]. */
  protected open fun check(challenge: Challenge, given: String?): Boolean = given?.trim() == challenge.answer

  private fun current(): Challenge = Session.get().getMetaData(KEY) ?: renew()

  private fun renew(): Challenge = newChallenge().also { Session.get().setMetaData(KEY, it); Session.get().bind() }

  override fun addComponents() {
    +Label("question", object : IModel<String> { override fun getObject(): String = current().question })
    +answer.apply { setRequired(true); add(org.apache.wicket.AttributeModifier.replace("autocomplete", "off")) }
  }

  override fun labelTarget(): FormComponent<*> = answer

  override fun convertInput() {
    val passed = check(current(), answer.convertedInput)
    if (!passed) {
      error(ValidationError().addKey("wrong").setMessage("That is not the right answer."))
      renew()
    }
    convertedInput = passed
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-captcha")

  companion object {
    private val RANDOM = SecureRandom()
    private val KEY = object : MetaDataKey<Challenge>() {}
  }
}
