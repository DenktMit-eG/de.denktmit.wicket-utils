package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/**
 * A jump to a region of the same page (requirement D.38): an `a href="#id"` to the [target] component, which is
 * highlighted by the `:target` rule of the anchor sheet, no script involved. The link's body is the author's
 * markup or [label].
 */
open class DmAnchorLink
@JvmOverloads
constructor(
  id: String,
  private val target: Component,
  label: IModel<String>? = null,
) : WebMarkupContainer(id, label) {

  constructor(id: String, target: Component, label: String) : this(id, target, Model.of(label))

  init {
    target.outputMarkupId = true
  }

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    target.add(org.apache.wicket.AttributeModifier.append("class", "dm-anchor-target"))
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    checkComponentTag(tag, "a")
    tag.put("href", "#" + target.markupId)
  }

  override fun onComponentTagBody(markupStream: org.apache.wicket.markup.MarkupStream, openTag: ComponentTag) {
    val label = defaultModelObjectAsString
    if (label.isNotEmpty()) replaceComponentTagBody(markupStream, openTag, org.apache.wicket.util.string.Strings.escapeMarkup(label)) else super.onComponentTagBody(markupStream, openTag)
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.css("dm-anchor-link")))
  }
}
