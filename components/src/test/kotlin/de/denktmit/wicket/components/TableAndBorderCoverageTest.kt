package de.denktmit.wicket.components

import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.border.Border
import de.denktmit.wicket.components.border.ListBorder
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListItem
import de.denktmit.wicket.components.table.DmGroupTableContainer
import de.denktmit.wicket.components.table.DmTableContainer
import de.denktmit.wicket.components.table.DynamicTableColumn
import de.denktmit.wicket.components.table.DynamicTableContainer
import de.denktmit.wicket.components.table.TableColumn
import de.denktmit.wicket.components.table.TableRowData
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class TableAndBorderCoverageTest : WicketTestBase() {

  data class Row(var name: String, var count: Int) : TableRowData<Row>

  @Test
  fun `table columns support getter setter and custom cell builder`() {
    val row = Row("A", 1)
    val mutableColumn = TableColumn(Row::name, "Name")
    val readonlyColumn = TableColumn<Row, Int>("readonly", getter = { it.count })
    val customColumn = TableColumn<Row, Int>("count", getter = { it.count }, setter = { r, v -> r.count = v }) { _, _, value ->
      add(DmLabel("inner", value.toString()))
    }

    assertThat(mutableColumn[row]).isEqualTo("A")
    mutableColumn[row] = "B"
    assertThat(row.name).isEqualTo("B")

    assertThatThrownBy { readonlyColumn[row] = 3 }
      .isInstanceOf(RuntimeException::class.java)
      .hasMessageContaining("Setter is not available")

    val customCell = customColumn.createCellComponent(row) as DmContainer
    invokeDeclared(customCell, "onInitialize")
    assertThat(customCell.get("inner")).isNotNull
  }

  @Test
  fun `dynamic table column supports default and custom rendering`() {
    val row = Row("A", 1)
    val item = DmListItem(0, Model.of(row))
    val defaultColumn = DynamicTableColumn<Row, String>("name", getter = { it.name })
    val customColumn =
      DynamicTableColumn<Row, String>(
        "name",
        getter = { it.name },
        setter = { r, v -> r.name = v },
      ) { _, _, value ->
        +DmLabel("custom", value)
      }

    assertThat(defaultColumn.createCellComponent(item, row)).isInstanceOf(DmLabel::class.java)
    assertThat(customColumn.createCellComponent(item, row)).isEqualTo(Unit)

    customColumn[row] = "B"
    assertThat(customColumn[row]).isEqualTo("B")
    assertThatThrownBy { defaultColumn[row] = "C" }
      .isInstanceOf(RuntimeException::class.java)
      .hasMessageContaining("Setter is not available")
  }

  @Test
  fun `table containers initialize header and body nodes`() {
    val rows = Model.ofList(listOf(Row("A", 1)))
    val columns = listOf(TableColumn<Row, String>("name", getter = { it.name }))
    val table = DmTableContainer("table", rows, columns)
    val groupedTable =
      DmGroupTableContainer(
        "groupTable",
        Model.ofList(listOf(DmGroupTableContainer.TableGroup("Group", Row("A", 1)))),
        columns,
      )
    val dynamicTable =
      DynamicTableContainer(
        "dynamicTable",
        rows,
        listOf(DynamicTableColumn<Row, String>("name", getter = { it.name })),
      )

    invokeDeclared(table, "onInitialize")
    invokeDeclared(groupedTable, "onInitialize")
    invokeDeclared(dynamicTable, "onInitialize")

    assertThat(table.get("header")).isNotNull
    assertThat(table.get("body")).isNotNull
    assertThat(groupedTable.get("header")).isNotNull
    assertThat(groupedTable.get("body")).isNotNull
    assertThat(dynamicTable.get("columns")).isNotNull
    assertThat(dynamicTable.get("rows")).isNotNull
  }

  @Test
  fun `row data default operators delegate to table columns`() {
    val row = Row("A", 1)
    val countColumn = TableColumn<Row, Int>("count", getter = { it.count }, setter = { r, v -> r.count = v })

    row[countColumn] = 7

    assertThat(row[countColumn]).isEqualTo(7)
    assertThat(row.id).isNotBlank
  }

  @Test
  fun `table row data default impls class is executed`() {
    val row = Row("A", 1)
    val countColumn = TableColumn<Row, Int>("count", getter = { it.count }, setter = { r, v -> r.count = v })
    val defaultImpls = Class.forName("de.denktmit.wicket.components.table.TableRowData\$DefaultImpls")

    defaultImpls
      .getDeclaredMethod("set", TableRowData::class.java, TableColumn::class.java, Any::class.java)
      .invoke(null, row, countColumn, 11)
    val value =
      defaultImpls
        .getDeclaredMethod("get", TableRowData::class.java, TableColumn::class.java)
        .invoke(null, row, countColumn)
    val id = defaultImpls.getDeclaredMethod("getId", TableRowData::class.java).invoke(null, row) as String

    assertThat(value).isEqualTo(11)
    assertThat(id).isNotBlank
  }

  @Test
  fun `border and list border helper methods delegate to underlying containers`() {
    val border = object : Border("border") {}
    border.enableAjax()
    border.add(DmLabel("inner", "value"))
    border.remove("inner")
    border.addOrReplace(DmLabel("inner", "value"))
    border.replace(DmLabel("inner", "value"))
    border.addToBorder(DmLabel("outside", "value"))
    border.replaceInBorder(DmLabel("outside", "value-updated"))
    border.removeFromBorder(border.get("outside"))

    assertThat(border.outputMarkupId).isTrue
    assertThat(border.outputMarkupPlaceholderTag).isTrue

    val listBorder =
      object : ListBorder<Row, List<Row>>("listBorder", Model.ofList(listOf(Row("A", 1))), { }) {}
    listBorder.enableAjax()
    listBorder.addToBorder(DmLabel("outside", "value"))
    listBorder.replaceInBorder(DmLabel("outside", "value-updated"))
    listBorder.removeFromBorder(listBorder.get("outside"))

    assertThatThrownBy { listBorder.add(DmLabel("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }
}
