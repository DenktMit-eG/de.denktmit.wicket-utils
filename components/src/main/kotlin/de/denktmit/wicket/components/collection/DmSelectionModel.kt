package de.denktmit.wicket.components.collection

import org.apache.wicket.ajax.AjaxRequestTarget
import java.io.Serializable

/** How many rows a [DmSelectionModel] holds. */
enum class SelectionMode { NONE, SINGLE, MULTI }

/**
 * The selection of a collection widget as a model the page reads and sets (requirement F.64): one row, many rows
 * or none, with [onChange] called after every change together with the Ajax target when there is one, so the page
 * can refresh what depends on the selection.
 */
open class DmSelectionModel<T>
@JvmOverloads
constructor(
  val mode: SelectionMode = SelectionMode.SINGLE,
  initial: Collection<T> = emptyList(),
) : Serializable {

  private val items = LinkedHashSet<T>(initial)

  /** Called after the selection changed. */
  @Transient
  var onChange: ((Set<T>, AjaxRequestTarget?) -> Unit)? = null

  /** The selected rows, in selection order. */
  val selected: Set<T> get() = java.util.Collections.unmodifiableSet(items)

  /** The single selected row, `null` for none (the first for a multi selection). */
  val single: T? get() = items.firstOrNull()

  /** Whether [item] is selected. */
  fun isSelected(item: T): Boolean = item in items

  /** Selects [item]; in single mode it replaces the selection. */
  @JvmOverloads
  fun select(item: T, target: AjaxRequestTarget? = null) {
    if (mode == SelectionMode.NONE) return
    if (mode == SelectionMode.SINGLE) items.clear()
    items.add(item)
    onChange?.invoke(selected, target)
  }

  /** Deselects [item]. */
  @JvmOverloads
  fun deselect(item: T, target: AjaxRequestTarget? = null) {
    if (items.remove(item)) onChange?.invoke(selected, target)
  }

  /** Selects [item] when it is not selected and deselects it otherwise. */
  @JvmOverloads
  fun toggle(item: T, target: AjaxRequestTarget? = null) {
    if (isSelected(item)) deselect(item, target) else select(item, target)
  }

  /** Replaces the selection with [all] (as many as the mode allows). */
  @JvmOverloads
  fun set(all: Collection<T>, target: AjaxRequestTarget? = null) {
    items.clear()
    when (mode) {
      SelectionMode.NONE -> {}
      SelectionMode.SINGLE -> all.firstOrNull()?.let { items.add(it) }
      SelectionMode.MULTI -> items.addAll(all)
    }
    onChange?.invoke(selected, target)
  }

  /** Clears the selection. */
  @JvmOverloads
  fun clear(target: AjaxRequestTarget? = null) {
    if (items.isEmpty()) return
    items.clear()
    onChange?.invoke(selected, target)
  }
}
