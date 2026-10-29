package de.denktmit.wicket.components.behavior

import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.CallbackParameter
import org.apache.wicket.AttributeModifier
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.model.IModel
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem

/**
 * Makes a widget draggable (requirement I.88) under a [key] the drop target reports; drags are HTML5 drag and
 * drop by script, and [group] keeps sources and targets of one kind together.
 */
class DragSourceBehavior
@JvmOverloads
constructor(
  val key: String,
  val group: String = "default",
) : Behavior() {
  override fun onComponentTag(component: Component, tag: ComponentTag) {
    tag.put("draggable", "true")
    tag.put("data-dm-drag-key", key)
    tag.put("data-dm-drag-group", group)
  }

  override fun renderHead(component: Component, response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(DmResources.css("dm-drag-drop")))
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
    response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-drag-drop")))
  }
}

/**
 * Makes a widget a drop target (requirement I.88): when a source of the same [group] is dropped on it, the page
 * is told which key was dropped where through [onDrop], and the server decides what happens.
 */
class DropTargetBehavior
@JvmOverloads
constructor(
  val group: String = "default",
  @Transient
  private val onDrop: (key: String, target: AjaxRequestTarget) -> Unit,
) : AbstractDefaultAjaxBehavior() {

  override fun respond(target: AjaxRequestTarget) {
    val key = RequestCycle.get().request.requestParameters.getParameterValue("key").toString("")
    onDrop(key, target)
  }

  override fun onBind() {
    super.onBind()
    component.add(AttributeModifier.replace("data-dm-drop-group", group))
    component.add(AttributeModifier.replace("data-dm-drop-call", object : IModel<String> { override fun getObject(): String = getCallbackFunction(CallbackParameter.explicit("key")).toString() }))
  }

  override fun renderHead(component: Component, response: IHeaderResponse) {
    super.renderHead(component, response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-drag-drop")))
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
    response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-drag-drop")))
  }
}

/** Makes this component draggable under [key]. */
@JvmOverloads
fun <C : Component> C.dragSource(key: String, group: String = "default"): C = apply { add(DragSourceBehavior(key, group)) }

/** Makes this component a drop target that reports the dropped key to [onDrop]. */
@JvmOverloads
fun <C : Component> C.dropTarget(group: String = "default", onDrop: (String, AjaxRequestTarget) -> Unit): C = apply { add(DropTargetBehavior(group, onDrop)) }
