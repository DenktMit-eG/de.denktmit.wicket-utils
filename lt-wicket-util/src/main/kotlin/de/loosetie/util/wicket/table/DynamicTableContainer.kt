package de.loosetie.util.wicket.table

import de.loosetie.util.wicket.component.MyContainer
import de.loosetie.util.wicket.component.MyLabel
import de.loosetie.util.wicket.component.MyListItem
import de.loosetie.util.wicket.component.MyListView
import de.loosetie.util.wicket.modelOf
import org.apache.wicket.model.IModel
import java.io.Serializable

class DynamicTableContainer<RD : Any>(
  id: String,
  val provider: IModel<List<RD>>,
  val columns: List<DynamicTableColumn<RD, *>>,
) : MyContainer(id) {
  override fun onInitialize() {
    super.onInitialize()

    +MyListView("columns", modelOf { columns }) { column ->
      +MyLabel("column", modelOf { column.name })
    }
    +MyListView("rows", provider) { data ->
      +MyListView("cell", modelOf { columns }) { column ->
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
  val initCell: (MyListItem<*>.(DynamicTableColumn<RD, VALUE>, RD, VALUE) -> Unit)? = null,
) : Serializable {
  operator fun get(row: RD): VALUE = getter.invoke(row)

  operator fun set(
    row: RD,
    value: VALUE,
  ): Unit =
    setter?.invoke(row, value)
      ?: throw RuntimeException("Setter is not available (id='$id', getter='$getter'")

  fun createCellComponent(
    item: MyListItem<*>,
    row: RD,
  ) = initCell?.let { initFn ->
    item.initFn(this@DynamicTableColumn, row, getter(row))
  } ?: MyLabel(id, getter(row).toString())
}
