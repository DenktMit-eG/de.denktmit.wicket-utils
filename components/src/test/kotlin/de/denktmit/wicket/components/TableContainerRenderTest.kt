package de.denktmit.wicket.components

import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.base.unaryPlus
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListItem
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.components.table.DmGroupTableContainer
import de.denktmit.wicket.components.table.DmTableContainer
import de.denktmit.wicket.components.table.DynamicTableColumn
import de.denktmit.wicket.components.table.DynamicTableContainer
import de.denktmit.wicket.components.table.TableColumn
import de.denktmit.wicket.components.table.TableRowData
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TableContainerRenderTest : WicketTestBase() {

  data class Row(var name: String, var count: Int = 0) : TableRowData<Row>

  @Test
  fun `DmTableContainer onInitialize creates header labels and body list view`() {
    val columns = listOf(
      TableColumn<Row, String>("name", "Name", getter = { it.name }),
      TableColumn<Row, Int>("count", "Count", getter = { it.count }),
    )
    val rows = Model.ofList(listOf(Row("Alice", 1), Row("Bob", 2)))
    val table = DmTableContainer("table", rows, columns)

    invokeDeclared(table, "onInitialize")

    val header = table.get("header") as DmContainer
    val body = table.get("body") as DmContainer
    assertThat(header).isNotNull
    assertThat(body).isNotNull
  }

  @Test
  fun `DmTableContainer KProperty constructor works`() {
    class Holder {
      val items: List<Row> = listOf(Row("A"))
    }
    val holder = Holder()
    val columns = listOf(TableColumn<Row, String>("name", getter = { it.name }))
    val table = DmTableContainer(holder::items, columns)

    assertThat(table.id).isEqualTo("items")
    assertThat(table.columns).hasSize(1)
  }

  @Test
  fun `DmGroupTableContainer onInitialize creates header and grouped body`() {
    val columns = listOf(
      TableColumn<Row, String>("name", "Name", getter = { it.name }),
    )
    val groups = Model.ofList(
      listOf(
        DmGroupTableContainer.TableGroup("Group A", Row("Alice"), Row("Bob")),
        DmGroupTableContainer.TableGroup("Group B", Row("Charlie")),
      )
    )
    val table = DmGroupTableContainer("groups", groups, columns)

    invokeDeclared(table, "onInitialize")

    assertThat(table.get("header")).isNotNull
    assertThat(table.get("body")).isNotNull
  }

  @Test
  fun `DmGroupTableContainer KProperty constructor works`() {
    class Holder {
      val groups: List<DmGroupTableContainer.TableGroup<Row>> =
        listOf(DmGroupTableContainer.TableGroup("G", Row("A")))
    }
    val holder = Holder()
    val columns = listOf(TableColumn<Row, String>("name", getter = { it.name }))
    val table = DmGroupTableContainer(holder::groups, columns)

    assertThat(table.id).isEqualTo("groups")
  }

  @Test
  fun `DynamicTableContainer onInitialize creates column headers and row cells`() {
    val columns = listOf(
      DynamicTableColumn<Row, String>("name", "Name", getter = { it.name }),
      DynamicTableColumn<Row, Int>("count", "Count", getter = { it.count }),
    )
    val rows = Model.ofList(listOf(Row("Alice", 1)))
    val table = DynamicTableContainer("dynamic", rows, columns)

    invokeDeclared(table, "onInitialize")

    assertThat(table.get("columns")).isNotNull
    assertThat(table.get("rows")).isNotNull
  }

  @Test
  fun `DynamicTableColumn with custom initCell invokes it`() {
    var cellCreated = false
    val column = DynamicTableColumn<Row, String>(
      "name",
      getter = { it.name },
      initCell = { _, _, value ->
        cellCreated = true
        +DmLabel("cell", value)
      }
    )
    val row = Row("Test")
    val item = DmListItem(0, Model.of(row))

    column.createCellComponent(item, row)

    assertThat(cellCreated).isTrue()
  }

  @Test
  fun `DynamicTableColumn without initCell returns DmLabel`() {
    val column = DynamicTableColumn<Row, String>("name", getter = { it.name })
    val row = Row("Test")
    val item = DmListItem(0, Model.of(row))

    val result = column.createCellComponent(item, row)

    assertThat(result).isInstanceOf(DmLabel::class.java)
  }

  @Test
  fun `TableColumn with initCell creates DmContainer`() {
    val column = TableColumn<Row, String>(
      "name",
      getter = { it.name },
      initCell = { _, _, value ->
        +DmLabel("inner", value)
      }
    )
    val row = Row("Hello")

    val cell = column.createCellComponent(row)

    assertThat(cell).isInstanceOf(DmContainer::class.java)
  }

  @Test
  fun `TableColumn without initCell creates DmLabel`() {
    val column = TableColumn<Row, String>("name", getter = { it.name })
    val row = Row("Hello")

    val cell = column.createCellComponent(row)

    assertThat(cell).isInstanceOf(DmLabel::class.java)
  }

  @Test
  fun `TableColumn KProperty1 constructor creates getter and setter`() {
    val column = TableColumn(Row::name, "Name")
    val row = Row("Original")

    assertThat(column[row]).isEqualTo("Original")
    column[row] = "Updated"
    assertThat(row.name).isEqualTo("Updated")
    assertThat(column.id).isEqualTo("name")
    assertThat(column.name).isEqualTo("Name")
  }

  @Test
  fun `TableGroup rows are mutable`() {
    val group = DmGroupTableContainer.TableGroup("G", Row("A"))
    group.rows.add(Row("B"))

    assertThat(group.rows).hasSize(2)
    assertThat(group.name).isEqualTo("G")
  }
}
