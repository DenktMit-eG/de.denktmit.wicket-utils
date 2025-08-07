package de.denktmit.wicket.components.table

import de.denktmit.wicket.components.component.DmContainer
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListItem
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.model.IModel
import java.io.Serializable

class DynamicTableContainer<RD : Any>(
  id: String,
  val provider: IModel<List<RD>>,
  val columns: List<DynamicTableColumn<RD, *>>,
) : DmContainer(id) {
  override fun onInitialize() {
    super.onInitialize()

    +DmListView("columns", modelOf { columns }) { column ->
      +DmLabel("column", modelOf { column.name })
    }
    +DmListView("rows", provider) { data ->
      +DmListView("cell", modelOf { columns }) { column ->
        column.createCellComponent(this, data)
      }
    }
  }
}

class DynamicTableColumn<RD, VALUE>(
  val id: String,
  val name: String = id,
  @Transient
  val getter: (RD) -> VALUE,
  @Transient
  val setter: ((RD, VALUE) -> Unit)? = null,
  @Transient
  val initCell: (DmListItem<*>.(DynamicTableColumn<RD, VALUE>, RD, VALUE) -> Unit)? = null,
) : Serializable {
  operator fun get(row: RD): VALUE = getter.invoke(row)

  operator fun set(
    row: RD,
    value: VALUE,
  ): Unit =
    setter?.invoke(row, value)
      ?: throw RuntimeException("Setter is not available (id='$id', getter='$getter'")

  fun createCellComponent(
    item: DmListItem<*>,
    row: RD,
  ) = initCell?.let { initFn ->
    item.initFn(this@DynamicTableColumn, row, getter(row))
  } ?: DmLabel(id, getter(row).toString())
}
