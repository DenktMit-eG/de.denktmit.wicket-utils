package de.denktmit.wicket.components.behavior

import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/**
 * Asks [question] before the action runs (requirement I.92): a capture-phase click listener by script stops the
 * click when the user says no, for plain and Ajax links and buttons alike. Without script the action runs; the
 * question is comfort, the action is the behaviour.
 */
class ConfirmBehavior(private val question: IModel<String>) : Behavior() {

  constructor(question: String) : this(Model.of(question))

  override fun onComponentTag(component: Component, tag: ComponentTag) {
    tag.put("data-dm-confirm", question.`object`)
  }

  override fun renderHead(component: Component, response: IHeaderResponse) {
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
    response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-confirm")))
  }
}

/** Asks [question] before this component's action runs. */
fun <C : Component> C.confirm(question: String): C = apply { add(ConfirmBehavior(question)) }
