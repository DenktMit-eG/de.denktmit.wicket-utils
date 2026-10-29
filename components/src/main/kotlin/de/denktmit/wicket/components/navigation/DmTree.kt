package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.collection.DmSelectionModel
import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.Component
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.CallbackParameter
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider
import org.apache.wicket.extensions.markup.html.repeater.tree.NestedTree
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.CheckBox
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import java.util.Optional

/**
 * Nodes with children loaded on open (requirement D.35), a thin layer over Wicket's `NestedTree`: expand and
 * collapse by link (Ajax with fallback), selection of one or many through a [DmSelectionModel], check boxes with
 * a tri-state parent, an icon per node, and drag of nodes by script with the drop reported to [onDrop]. The
 * provider is Wicket's `ITreeProvider`, lazy by nature; [key] names a node in the markup for selection and drag.
 */
open class DmTree<T : java.io.Serializable>
@JvmOverloads
constructor(
  id: String,
  provider: ITreeProvider<T>,
  /** The selection; `null` for a tree that only shows. */
  val selection: DmSelectionModel<T>? = null,
  /** Show a check box per node; the checked nodes live in [checked]. */
  val checkable: Boolean = false,
  val checked: MutableSet<T> = LinkedHashSet(),
  @Transient
  private val label: (T) -> String = { it.toString() },
  @Transient
  private val icon: ((T) -> String?)? = null,
  @Transient
  private val key: (T) -> String = { it.toString() },
  var draggable: Boolean = false,
) : NestedTree<T>(id, provider) {

  /** Called when a dragged node was dropped on another: (dragged, target). The server decides what happens. */
  @Transient
  var onDrop: ((T, T, AjaxRequestTarget) -> Unit)? = null

  private val nodes = HashMap<String, T>()

  private val drop = object : AbstractDefaultAjaxBehavior() {
    override fun respond(target: AjaxRequestTarget) {
      val parameters = requestCycle.request.requestParameters
      val dragged = nodes[parameters.getParameterValue("dragged").toString("")]
      val onto = nodes[parameters.getParameterValue("target").toString("")]
      if (dragged != null && onto != null) onDrop?.invoke(dragged, onto, target)
      target.add(this@DmTree)
    }
  }

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    outputMarkupId = true
    add(drop)
  }

  /** The node rendered under [key]. */
  fun node(key: String): T? = nodes[key]

  /** Checks or unchecks [node] and every child the provider knows, then fixes the ancestors. */
  fun check(node: T, on: Boolean) {
    fun walk(n: T) {
      if (on) checked.add(n) else checked.remove(n)
      if (provider.hasChildren(n)) provider.getChildren(n).forEach { walk(it) }
    }
    walk(node)
  }

  /** `true` when every known child is checked, `false` when none, `null` when some. */
  fun checkState(node: T): Boolean? {
    if (!provider.hasChildren(node)) return node in checked
    val states = provider.getChildren(node).asSequence().map { checkState(it) }.toList()
    return when {
      states.all { it == true } -> true
      states.all { it == false } && node !in checked -> false
      else -> null
    }
  }

  override fun newContentComponent(id: String, model: IModel<T>): Component = NodePanel(id, model)

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", "tree")
    tag.put("data-dm-tree", "")
    if (draggable) tag.put("data-dm-tree-drop", drop.getCallbackFunction(CallbackParameter.explicit("dragged"), CallbackParameter.explicit("target")))
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    response.render(CssHeaderItem.forReference(DmResources.css("dm-tree")))
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
    response.render(JavaScriptHeaderItem.forReference(DmResources.js("dm-tree")))
  }

  /** The content of one node: toggle, check box, icon and label as a selectable link. */
  inner class NodePanel(id: String, model: IModel<T>) : Panel(id, model) {
    @Suppress("UNCHECKED_CAST")
    private val node: T get() = defaultModelObject as T

    override fun onInitialize() {
      super.onInitialize()
      nodes[key(node)] = node
      add(object : AjaxFallbackLink<Void>("toggle") {
        override fun onClick(target: Optional<AjaxRequestTarget>) {
          if (getState(node) == State.EXPANDED) collapse(node) else expand(node)
          target.ifPresent { updateBranch(node, it) }
        }

        override fun onConfigure() {
          super.onConfigure()
          isVisible = provider.hasChildren(node)
        }

        override fun onComponentTag(tag: ComponentTag) {
          super.onComponentTag(tag)
          tag.put("aria-label", if (getState(node) == State.EXPANDED) "Collapse" else "Expand")
        }
      }.apply { add(DmIcon("toggleIcon", "chevron-right")) })
      add(object : CheckBox("check", object : IModel<Boolean> {
        override fun getObject(): Boolean = checkState(node) == true

        override fun setObject(value: Boolean) = check(node, value)
      }) {
        override fun onConfigure() {
          super.onConfigure()
          isVisible = checkable
        }

        override fun onComponentTag(tag: ComponentTag) {
          super.onComponentTag(tag)
          if (checkState(node) == null) tag.put("data-dm-indeterminate", "")
          tag.put("aria-label", label(node))
        }
      }.apply {
        add(object : OnChangeAjaxBehavior() {
          override fun onUpdate(target: AjaxRequestTarget) {
            target.add(this@DmTree)
          }
        })
      })
      add(object : AjaxFallbackLink<Void>("select") {
        override fun onClick(target: Optional<AjaxRequestTarget>) {
          selection?.toggle(node, target.orElse(null))
          target.ifPresent { it.add(this@DmTree) }
        }

        override fun onConfigure() {
          super.onConfigure()
          isEnabled = selection != null
        }

        override fun onComponentTag(tag: ComponentTag) {
          super.onComponentTag(tag)
          if (selection?.isSelected(node) == true) tag.put("aria-selected", "true")
        }
      }.apply {
        add(DmIcon("icon", icon?.invoke(node) ?: "").apply { isVisible = icon?.invoke(node) != null })
        add(Label("label", label(node)))
      })
    }

    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("role", "treeitem")
      tag.put("data-dm-tree-node", key(node))
      if (provider.hasChildren(node)) tag.put("aria-expanded", (getState(node) == State.EXPANDED).toString())
      if (selection?.isSelected(node) == true) tag.append("class", "dm-tree-selected", " ")
      if (draggable) tag.put("draggable", "true")
    }
  }
}
