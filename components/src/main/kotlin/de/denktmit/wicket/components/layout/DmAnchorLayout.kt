package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.request.resource.ResourceReference

/** The edge or corner a child of [DmAnchorLayout] sticks to. */
enum class Anchor(val cssClass: String) {
  TOP_START("dm-anchor-top-start"), TOP("dm-anchor-top"), TOP_END("dm-anchor-top-end"),
  START("dm-anchor-start"), CENTER("dm-anchor-center"), END("dm-anchor-end"),
  BOTTOM_START("dm-anchor-bottom-start"), BOTTOM("dm-anchor-bottom"), BOTTOM_END("dm-anchor-bottom-end"),
}

/**
 * Children positioned relative to the container's edges and corners (requirement C.23), kept there when the
 * container resizes. The container is the positioning context; each child is anchored through [anchor] and
 * offset by [inset].
 */
open class DmAnchorLayout
@JvmOverloads
constructor(
  id: String,
  /** The height of the container, a CSS length; the children do not size it. */
  var height: String = "20rem",
  /** The distance of an anchored child from its edge. */
  var inset: Space = Space.S0,
  @Transient
  private val setup: DmAnchorLayout.() -> Unit = {},
) : DmLayoutContainer(id) {

  /** Sticks [child] to [anchor]. */
  fun <C : Component> anchor(child: C, anchor: Anchor): C = child.also { it.addCssClass("dm-anchored", anchor.cssClass) }

  override fun onInitialize() {
    super.onInitialize()
    setup()
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-anchor-layout")

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("style", "--height:$height;--inset:${inset.css}", ";")
  }
}
