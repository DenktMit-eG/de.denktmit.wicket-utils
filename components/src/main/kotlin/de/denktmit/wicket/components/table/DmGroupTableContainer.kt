package de.denktmit.wicket.components.table

import de.denktmit.wicket.components.component.DmContainer
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.model.IModel
import java.io.Serializable
import kotlin.reflect.KProperty0

/**
 * Default structure
 *
 * ```
 * <table wicket:id="[id]">
 *   <thead wicket:id="head">
 *     <tr>
 *       <td wicket:id="[column_id]"></td>
 *     </tr>
 *   </thead>
 *   <tbody wicket:id="body"> // will be rendered for each group
 *     <tr>
 *       <td colspan="[columns.size()]" wicket:id="groupHeader">Group Title</td>
 *     </tr>
 *     <tr wicket:id="rows">
 *       <td wicket:id="[column_id]"></td>
 *     </tr>
 *   </thead>
 * </table>
 * ```
 *
 * Replace all values in [...] with the matching ids of your components
 */
class DmGroupTableContainer<RD : TableRowData<RD>>(
  id: String,
  val provider: IModel<List<TableGroup<RD>>>,
  val columns: List<TableColumn<RD, *>>,
) : DmContainer(id) {
  class TableGroup<RD : TableRowData<RD>>(
    val name: String,
    var rows: MutableList<RD>,
  ) : Serializable {
    constructor(name: String, vararg rows: RD) : this(name, rows.toMutableList())
  }

  constructor(
    field: KProperty0<List<TableGroup<RD>>>,
    columns: List<TableColumn<RD, *>>,
    id: String = field.name,
    provider: IModel<List<TableGroup<RD>>> = modelOf(field),
  ) : this(id, provider, columns)

  override fun onInitialize() {
    super.onInitialize()

    +DmContainer("header") {
      columns.forEach { c ->
        +DmLabel(c.id, modelOf { c.name })
      }
    }
    +DmListView("body", provider) {
      +DmLabel("groupHeader", modelObject.name)
      +DmListView("rows", modelOf(modelObject::rows)) { data ->
        columns.forEach { c ->
          +DmContainer(c.id) {
            c.createCellComponent(data)
          }
        }
      }
    }
  }
}
