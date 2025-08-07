package de.denktmit.wicket.components.table

import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.base.unaryPlus
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

/**
 * Default structure
 *
 * ```
 * <table wicket:id="[id]">
 *   <thead wicket:id="header">
 *     <tr>
 *       <td wicket:id="[column_id]"></td>
 *     </tr>
 *   </thead>
 *   <tbody wicket:id="body">
 *     <tr wicket:id="rows">
 *       <td wicket:id="[column_id]"></td>
 *     </tr>
 *   </tbody>
 * </table>
 * ```
 *
 * Replace all values in [...] with the matching ids of your components
 */
class DmTableContainer<RD : Any>(
  id: String,
  val provider: IModel<List<RD>>,
  val columns: List<TableColumn<RD, *>>,
) : DmContainer(id) {
  constructor(
    field: KProperty0<List<RD>>,
    columns: List<TableColumn<RD, *>>,
    id: String = field.name,
    provider: IModel<List<RD>> = modelOf(field),
  ) : this(id, provider, columns)

  override fun onInitialize() {
    super.onInitialize()

    +DmContainer("header") {
      columns.forEach { c ->
        +DmLabel(c.id, modelOf { c.name })
      }
    }
    +DmContainer("body") {
      +DmListView("rows", provider) { data ->
        columns.forEach { c ->
          +c.createCellComponent(data)
        }
      }
    }
  }
}
