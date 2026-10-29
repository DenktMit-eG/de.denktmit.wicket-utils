package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.request.resource.ResourceReference

/**
 * Children in rows and columns (requirement C.15): as many columns as fit above [minColumn], one column below it,
 * so the grid wraps on its own. A child spans several columns through [span]; the span is capped at the columns
 * available so a wide child never overflows.
 */
open class DmGrid
@JvmOverloads
constructor(
  id: String,
  /** The minimum width of a column, a CSS length. */
  var minColumn: String = "15rem",
  /** The gap between cells. */
  var space: Space = Space.S0,
  init: DmContainer.() -> Unit = {},
) : DmLayoutContainer(id, null, init) {

  /** Lets [child] span [columns] columns (1 to 6). */
  fun <C : Component> span(child: C, columns: Int): C {
    require(columns in 1..6) { "span must be between 1 and 6, was $columns" }
    return child.also { it.addCssClass("dm-span-$columns") }
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("grid")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-grid")

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.append("style", "--min-column:$minColumn;--space:${space.css}", ";")
  }
}
