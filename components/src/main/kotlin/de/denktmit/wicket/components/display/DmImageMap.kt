package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.widget.widgetHooks
import de.denktmit.wicket.components.navigation.pageLinkFor
import org.apache.wicket.Page
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.image.Image
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One clickable area of a [DmImageMap]: a shape (`rect`, `circle`, `poly`), its coordinates, where it leads, its text. */
data class ImageArea
@JvmOverloads
constructor(
  val shape: String,
  val coords: String,
  val alt: String,
  val href: String? = null,
  val page: Class<out Page>? = null,
  val parameters: PageParameters = PageParameters(),
) : Serializable

/**
 * An image from a URL or a resource with areas that act as links (requirement G.67). Without areas it is a plain
 * image with its alternative text.
 */
open class DmImageMap
@JvmOverloads
constructor(
  id: String,
  private val src: IModel<String>?,
  private val resource: ResourceReference?,
  val alt: IModel<String>,
  val areas: List<ImageArea> = emptyList(),
) : Panel(id) {

  /** An image from a URL. */
  @JvmOverloads
  constructor(id: String, src: String, alt: String, areas: List<ImageArea> = emptyList()) : this(id, Model.of(src), null, Model.of(alt), areas)

  /** An image from a resource. */
  @JvmOverloads
  constructor(id: String, resource: ResourceReference, alt: String, areas: List<ImageArea> = emptyList()) : this(id, null, resource, Model.of(alt), areas)

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    val mapName = markupId + "-map"
    val image = if (resource != null) Image("image", resource) else object : WebMarkupContainer("image") {
      override fun onComponentTag(tag: ComponentTag) {
        super.onComponentTag(tag)
        tag.put("src", src!!.`object`)
      }
    }
    image.add(object : org.apache.wicket.behavior.Behavior() {
      override fun onComponentTag(component: org.apache.wicket.Component, tag: ComponentTag) {
        tag.put("alt", alt.`object`)
        if (areas.isNotEmpty()) tag.put("usemap", "#$mapName")
      }
    })
    add(image)
    add(WebMarkupContainer("map").apply {
      isVisible = areas.isNotEmpty()
      add(org.apache.wicket.AttributeModifier.replace("name", mapName))
      add(object : ListView<ImageArea>("areas", areas) {
        override fun populateItem(item: ListItem<ImageArea>) {
          val area = item.modelObject
          item.add(org.apache.wicket.AttributeModifier.replace("shape", area.shape))
          item.add(org.apache.wicket.AttributeModifier.replace("coords", area.coords))
          item.add(org.apache.wicket.AttributeModifier.replace("alt", area.alt))
          item.add(org.apache.wicket.AttributeModifier.replace("href", area.page?.let { urlFor(pageLinkFor(it, area.parameters)).toString() } ?: area.href ?: "#"))
        }
      })
    })
  }
}
