package de.denktmit.wicket.components.collection

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import java.io.Serializable

/**
 * A data source over a list in memory (requirement F.63): answers "how many" and "rows from x to y with this
 * sort" like every source a collection widget reads, so a page can start with a list and move to a database-backed
 * `ISortableDataProvider` later without touching the widget. Sorting is by the comparators registered with
 * [sortBy]; an unregistered sort property leaves the order as it is.
 */
open class ListDataSource<T : Serializable, S>
@JvmOverloads
constructor(
  private val items: IModel<out List<T>>,
  private val comparators: MutableMap<S, Comparator<T>> = LinkedHashMap(),
) : SortableDataProvider<T, S>() {

  constructor(items: List<T>) : this(Model.ofList(items))

  /** Sorts by [property] with [comparator] when that property is the sort. */
  fun sortBy(property: S, comparator: Comparator<T>): ListDataSource<T, S> {
    comparators[property] = comparator
    return this
  }

  /** The whole list, sorted as the current sort says. */
  fun sorted(): List<T> {
    val sort = sort ?: return items.`object`
    val comparator = comparators[sort.property] ?: return items.`object`
    return items.`object`.sortedWith(if (sort.isAscending) comparator else comparator.reversed())
  }

  override fun iterator(first: Long, count: Long): Iterator<T> = sorted().drop(first.toInt()).take(count.toInt()).iterator()

  override fun size(): Long = items.`object`.size.toLong()

  override fun model(item: T): IModel<T> = Model.of(item)

  override fun detach() {
    items.detach()
  }
}
