package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.behavior.visibleIf
import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetBorder
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A titled block with an action area, a body and a footer (requirement C.19). Actions join the header toolbar
 * through [action]; the footer is any component with id [FOOTER] given to [footer]. [collapsible] renders the
 * header as a `summary` that folds the body without script; [maximisable] and [closable] add buttons that a few
 * lines of script serve (grow to the viewport, hide the card); without script they are absent, because they would
 * do nothing.
 */
open class DmCard
@JvmOverloads
constructor(
  id: String,
  title: IModel<String>,
  var collapsible: Boolean = false,
  var maximisable: Boolean = false,
  var closable: Boolean = false,
  init: DmWidgetBorder.() -> Unit = {},
) : DmWidgetBorder(id, title, init) {

  @JvmOverloads
  constructor(id: String, title: String, collapsible: Boolean = false, maximisable: Boolean = false, closable: Boolean = false, init: DmWidgetBorder.() -> Unit = {}) :
    this(id, Model.of(title), collapsible, maximisable, closable, init)

  private val actions = RepeatingView("actions")
  private var footer: Component = WebMarkupContainer(FOOTER).apply { isVisible = false }

  /** Adds an action to the header toolbar; [factory] receives the id the component must use. */
  fun action(factory: (String) -> Component): DmCard {
    actions.add(factory(actions.newChildId()))
    return this
  }

  /** Places [component] (id [FOOTER]) in the footer. */
  fun footer(component: Component): DmCard {
    require(component.id == FOOTER) { "footer id must be '$FOOTER', was '${component.id}'" }
    footer = component
    return this
  }

  override fun addComponents() {
    outputMarkupId = true
    val shell = object : WebMarkupContainer("shell") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.name = if (collapsible) "details" else "section"
        if (collapsible) tag.put("open", "open") else tag.remove("open")
      }
    }
    val header = object : WebMarkupContainer("header") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.name = if (collapsible) "summary" else "header"
      }
    }
    @Suppress("UNCHECKED_CAST")
    header.add(Label("title", defaultModel as IModel<String>))
    header.add(actions)
    header.add(WebMarkupContainer("maximize").visibleIf { maximisable }.apply { add(DmIcon("icon", "maximize")) })
    header.add(WebMarkupContainer("close").visibleIf { closable }.apply { add(DmIcon("icon", "close")) })
    shell.add(header)
    shell.add(footer)
    addToBorder(shell)
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-card")

  override fun script(): ResourceReference? = if (maximisable || closable) DmResources.js("dm-card") else null

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-card", "")
  }

  companion object {
    /** The id a footer component must carry. */
    const val FOOTER = "footer"
  }
}
