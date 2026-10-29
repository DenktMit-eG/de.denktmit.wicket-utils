package de.denktmit.wicket.components.collection

import org.apache.wicket.Component
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import java.io.Serializable

/**
 * One column of a [DmTable] (requirements F.59, F.65): a title, the sort property, how a cell's value is read, and
 * optionally a cell template, an in-place editor (the setter for the edited text), a total over the rows shown,
 * a CSS class, a hidden state the user may change and a frozen position.
 */
class DmColumn<T, S>
@JvmOverloads
constructor(
  val title: IModel<String>,
  /** The sort property; `null` for an unsortable column. */
  val sort: S? = null,
  @Transient
  val value: (T) -> Any?,
  /** A template for the cell; the component gets the id given and the row model. */
  @Transient
  val cell: ((String, IModel<T>) -> Component)? = null,
  /** Applies the edited text to the row; `null` for a read-only column. */
  @Transient
  val edit: ((T, String) -> Unit)? = null,
  /** The total over the rows of the page, `null` for none. */
  @Transient
  val total: ((List<T>) -> String)? = null,
  val cssClass: String? = null,
  var hidden: Boolean = false,
  val frozen: Boolean = false,
) : Serializable {

  @JvmOverloads
  constructor(title: String, sort: S? = null, value: (T) -> Any?) : this(Model.of(title), sort, value)

  /** The value of the cell as text. */
  fun text(row: T): String = value(row)?.toString() ?: ""
}
