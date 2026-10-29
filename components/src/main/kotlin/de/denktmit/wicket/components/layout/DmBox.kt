package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.request.resource.ResourceReference

/**
 * Children in a row or a column with a gap, alignment and wrapping (requirement C.14): a horizontal box is the
 * `cluster` primitive, a vertical box the `stack` primitive. A child takes the remaining space through [grow].
 * The children are the author's own markup inside the container's tag.
 */
abstract class DmBox
@JvmOverloads
constructor(
  id: String,
  /** The gap between the children. */
  var space: Space = Space.S0,
  /** Cross-axis alignment. */
  var align: Align = Align.CENTER,
  /** Main-axis distribution. */
  var justify: Justify = Justify.START,
  /** Whether the children wrap when the row runs out of space (rows only). */
  var wrap: Boolean = true,
  init: DmContainer.() -> Unit = {},
) : DmLayoutContainer(id, null, init) {

  /** Lets [child] take the space the other children leave. */
  fun <C : Component> grow(child: C): C = child.also { it.addCssClass("dm-grow") }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-box")

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("style", "--space:${space.css};--align:${align.css};--justify:${justify.css};--wrap:${if (wrap) "wrap" else "nowrap"}", ";")
  }
}

/** A row of children; see [DmBox]. */
open class DmHBox
@JvmOverloads
constructor(
  id: String,
  space: Space = Space.S0,
  align: Align = Align.CENTER,
  justify: Justify = Justify.START,
  wrap: Boolean = true,
  init: DmContainer.() -> Unit = {},
) : DmBox(id, space, align, justify, wrap, init) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("cluster")
  }
}

/** A column of children; see [DmBox]. */
open class DmVBox
@JvmOverloads
constructor(
  id: String,
  space: Space = Space.S0,
  align: Align = Align.STRETCH,
  justify: Justify = Justify.START,
  init: DmContainer.() -> Unit = {},
) : DmBox(id, space, align, justify, false, init) {
  override fun onInitialize() {
    super.onInitialize()
    addCssClass("stack")
  }
}
