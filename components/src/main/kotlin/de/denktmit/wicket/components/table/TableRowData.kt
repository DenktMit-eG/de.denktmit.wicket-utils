package de.denktmit.wicket.components.table

import java.io.Serializable
import java.util.concurrent.atomic.AtomicInteger

interface TableRowData<RD : TableRowData<RD>> : Serializable {
  val id: String
    get() = idSeed.addAndGet(1).toString()

  operator fun <T : Any> get(column: TableColumn<RD, T>): T = column[this as RD]

  operator fun <T : Any> set(
    column: TableColumn<RD, T>,
    value: T,
  ) {
    column[this as RD] = value
  }

  companion object {
    private val idSeed = AtomicInteger(0)
  }
}
