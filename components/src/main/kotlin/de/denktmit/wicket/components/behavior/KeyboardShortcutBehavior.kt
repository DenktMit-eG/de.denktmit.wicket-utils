package de.denktmit.wicket.components.behavior

import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.AttributeModifier
import org.apache.wicket.model.IModel
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem

/**
 * Binds a key combination to an action (requirement I.87): `Ctrl+S`, `Alt+Shift+N`, `Escape`, `?`. The page or the
 * widget the behaviour sits on is the scope: on a page the shortcut works anywhere, on a widget only while the
 * focus is inside it. Script fires the Ajax callback; without script the shortcut is silent, which is the one
 * thing a key binding cannot be.
 */
class KeyboardShortcutBehavior(
  val keys: String,
  @Transient
  private val action: (AjaxRequestTarget) -> Unit,
) : AbstractDefaultAjaxBehavior() {

  override fun respond(target: AjaxRequestTarget) = action(target)

  override fun onBind() {
    super.onBind()
    component.add(AttributeModifier.replace("data-dm-shortcut-keys", keys))
    component.add(AttributeModifier.replace("data-dm-shortcut-call", object : IModel<String> { override fun getObject(): String = getCallbackFunction().toString() }))
  }

  override fun renderHead(component: Component, response: IHeaderResponse) {
    super.renderHead(component, response)
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
    response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-shortcut")))
  }
}

/** Binds [keys] to [action] on this component (a page for a page-wide shortcut). */
fun <C : Component> C.shortcut(keys: String, action: (AjaxRequestTarget) -> Unit): C = apply { add(KeyboardShortcutBehavior(keys, action)) }
