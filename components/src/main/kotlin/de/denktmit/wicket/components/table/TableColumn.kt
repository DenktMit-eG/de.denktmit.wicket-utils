package de.denktmit.wicket.components.table

import de.denktmit.wicket.components.component.MyContainer
import de.denktmit.wicket.components.component.MyLabel
import java.io.Serializable
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class TableColumn<RD, VALUE>(
  val id: String,
  val name: String = id,
  @Transient
  val getter: (RD) -> VALUE,
  @Transient
  val setter: ((RD, VALUE) -> Unit)? = null,
  @Transient
  val initCell: (MyContainer.(TableColumn<RD, VALUE>, RD, VALUE) -> Unit)? = null,
) : Serializable {
  constructor(
    field: KProperty1<RD, VALUE>,
    name: String,
    id: String = field.name,
    initCell: (MyContainer.(TableColumn<RD, VALUE>, RD, VALUE) -> Unit)? = null,
  ) : this(
    id,
    name,
    { field.getter(it) },
    { r, v -> (field as? KMutableProperty1)?.setter?.invoke(r, v) },
    initCell,
  )

  operator fun get(row: RD): VALUE = getter.invoke(row)

  operator fun set(
    row: RD,
    value: VALUE,
  ): Unit =
    setter?.invoke(row, value)
      ?: throw RuntimeException("Setter is not available (id='$id', getter='$getter'")

  fun createCellComponent(row: RD) =
    initCell?.let { initFn ->
      MyContainer(id) {
        initFn(this@TableColumn, row, getter(row))
      }
    } ?: MyLabel(id, getter(row).toString())
}
