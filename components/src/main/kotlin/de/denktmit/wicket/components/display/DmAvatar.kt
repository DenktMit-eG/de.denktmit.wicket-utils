package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A picture or initials in a frame (requirement G.71): the image from [image] when there is one, else the initials
 * of [name]; the name is the accessible text either way. [size] is a CSS length.
 */
open class DmAvatar
@JvmOverloads
constructor(
  id: String,
  val name: IModel<String>,
  val image: IModel<String?>? = null,
  var size: String = "2.5rem",
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, name, init) {

  @JvmOverloads
  constructor(id: String, name: String, image: String? = null, size: String = "2.5rem") : this(id, Model.of(name), image?.let { Model.of(it) }, size)

  /** The initials of [name]: the first letters of the first and the last word, upper-cased. */
  fun initials(): String {
    val words = name.`object`.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when (words.size) {
      0 -> "?"
      1 -> words[0].take(1).uppercase()
      else -> (words.first().take(1) + words.last().take(1)).uppercase()
    }
  }

  private fun hasImage(): Boolean = !image?.`object`.isNullOrBlank()

  override fun addComponents() {
    +object : WebMarkupContainer("image") {
      override fun onConfigure() {
        super.onConfigure()
        isVisible = hasImage()
      }

      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("src", image?.`object` ?: "")
        tag.put("alt", name.`object`)
      }
    }
    +Label("initials", object : IModel<String> { override fun getObject(): String = initials() }).apply {
      add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: org.apache.wicket.Component) {
          component.isVisible = !hasImage()
        }
      })
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", "img")
    tag.put("aria-label", name.`object`)
    tag.append("style", "--avatar-size:$size", ";")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-display")
}
