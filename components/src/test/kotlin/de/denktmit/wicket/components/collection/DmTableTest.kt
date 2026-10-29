package de.denktmit.wicket.components.collection

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTableTest : WidgetTestBase() {

  /** The page-relative path of the row item of the member [name], whatever id the data view gave it this render. */
  private fun rowPath(table: DmTable<Member, String>, name: String): String =
    table.dataView.visitChildren(org.apache.wicket.markup.repeater.Item::class.java, org.apache.wicket.util.visit.IVisitor<org.apache.wicket.markup.repeater.Item<*>, String> { item, visit ->
      if ((item.defaultModelObject as? Member)?.name == name) visit.stop(item.pageRelativePath) else visit.dontGoDeeper()
    })

  private fun columns(edit: ((Member, String) -> Unit)? = null) = listOf(
    DmColumn<Member, String>(Model.of("Name"), "name", { it.name }, frozen = true),
    DmColumn<Member, String>(Model.of("Team"), null, { it.team }, cell = { id, model -> Label(id, model.map { "team " + it.team }) }, cssClass = "team"),
    DmColumn<Member, String>(Model.of("Rate"), "rate", { it.rate }, edit = edit, total = { rows -> rows.sumOf { it.rate }.toString() }),
  )

  @Test
  fun `renders headers, cells, templates, totals, frozen class and pages of five`() {
    val table = render(DmTable("t", columns(), source(), rowsPerPage = 5))
    assertHooks(table)
    assertThat(tags("headers")).hasSize(3)
    assertThat(tags("headers")[0].getAttribute("aria-sort")).isEqualTo("none")
    assertThat(tags("headers")[0].getAttribute("class")).contains("dm-table-frozen")
    assertThat(tags("headers")[1].getAttribute("class")).contains("team")
    assertThat(tags("sort")).hasSize(2)
    assertThat(tags("plainTitle")).hasSize(1)
    assertThat(tags("row")).hasSize(5)
    assertThat(html).contains("team blue")
    assertThat(tags("total").take(3).map { it.value }).containsExactly("", "", "550")
    assertThat(tester.getTagByWicketId("empty")).isNull()
    assertThat(tester.getTagByWicketId("select")).isNull()
    assertThat(tester.getTagByWicketId("toggle")).isNull()
    assertThat(tester.getTagByWicketId("columnMenu")).isNull()
    assertStyleSheet("dm-table")
    assertScript("dm-table")

    tester.clickLink("t:pager:navigator:next")
    assertThat(table.dataView.currentPage).isEqualTo(1)
    assertThat(tags("row")).hasSize(5)
  }

  @Test
  fun `sorting by a header toggles the direction and starts at page one`() {
    val table = render(DmTable("t", columns(), source(), rowsPerPage = 4))
    tester.clickLink("t:pager:navigator:next")
    tester.clickLink("t:headers:0:sort")
    assertThat(table.dataView.currentPage).isZero()
    assertThat(tags("headers")[0].getAttribute("aria-sort")).isEqualTo("ascending")
    assertThat(table.currentRows().map { it.name }).containsExactly("Anna", "Ben", "Carl", "Dora")
    tester.clickLink("t:headers:0:sort")
    assertThat(tags("headers")[0].getAttribute("aria-sort")).isEqualTo("descending")
    assertThat(table.currentRows().first().name).isEqualTo("Zed")
  }

  @Test
  fun `selection through the check column, detail rows, group headers and row classes`() {
    val selection = DmSelectionModel<Member>(SelectionMode.MULTI)
    val table = DmTable("t", columns(), source(members().sortedBy { it.team }), rowsPerPage = 12, selection = selection)
    table.detail = { id, model -> Label(id, model.map { "details of " + it.name }) }
    table.groupBy = { it.team }
    table.rowClass = { if (it.rate > 120) "high" else null }
    table.headerGroups = listOf("Person" to 2, "Money" to 1)
    render(table)
    assertThat(tags("groupTitle").map { it.value }).containsExactly("blue", "green", "red")
    assertThat(tags("group").map { it.value }).containsExactly("Person", "Money")
    assertThat(tags("group")[0].getAttribute("colspan")).isEqualTo("2")
    assertThat(tags("row").count { it.getAttribute("class").contains("high") }).isEqualTo(2)
    assertThat(tags("select")).hasSize(12)
    assertThat(tester.getTagByWicketId("detailRow")).isNull()

    tester.clickLink(rowPath(table, "Ben") + ":row:select")
    assertThat(selection.selected).hasSize(1)
    assertThat(tags("row").count { it.getAttribute("aria-selected") == "true" }).isEqualTo(1)
    assertThat(tags("select").count { it.getAttribute("aria-checked") == "true" }).isEqualTo(1)

    tester.clickLink(rowPath(table, "Anna") + ":row:toggle")
    assertThat(tags("detailRow")).hasSize(1)
    assertThat(html).contains("details of Anna")
    assertThat(tag("detail").getAttribute("colspan")).isEqualTo("5")
    tester.clickLink(rowPath(table, "Anna") + ":row:toggle")
    assertThat(tester.getTagByWicketId("detailRow")).isNull()
  }

  @Test
  fun `in-place editing writes through the column's editor`() {
    val rows = members()
    val table = render(DmTable("t", columns(edit = { m, text -> m.rate = text.toInt() }), source(rows), rowsPerPage = 3))
    assertThat(html).contains("dm-table-cell")
    val editable = table.visitChildren(org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel::class.java, org.apache.wicket.util.visit.IVisitor<org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel<*>, org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel<*>> { c, v -> v.stop(c) })
    assertThat(editable).isNotNull
    @Suppress("UNCHECKED_CAST")
    (editable!!.defaultModel as IModel<String>).`object` = "200"
    assertThat(rows[0].rate).isEqualTo(200)
  }

  @Test
  fun `empty source shows the message, the column menu hides columns`() {
    val table = DmTable("t", columns(), ListDataSource<Member, String>(emptyList()), rowsPerPage = 5).apply { columnMenu = true; emptyMessage = Model.of("No members") }
    render(table)
    assertThat(tag("empty").value).isEqualTo("No members")
    assertThat(tags("columnToggles")).hasSize(3)
    tester.newFormTester("t:columnMenu:columnsForm").apply { setValue("columnToggles:1:show", false); submit() }
    assertThat(table.columns[1].hidden).isTrue()
    assertThat(tags("headers")).hasSize(2)
  }

  @Test
  fun `ajax table refreshes itself on sort`() {
    render(DmTable("t", columns(), source(), rowsPerPage = 5, ajax = true))
    tester.clickLink("t:headers:0:sort", true)
    assertThat(tester.lastResponseAsString).contains("dm-table")
  }
}
