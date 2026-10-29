package de.denktmit.wicket.components.collection

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmListTest : WidgetTestBase() {

  @Test
  fun `items from the template with paging, sorting links, groups and selection with check marks`() {
    val selection = DmSelectionModel<Member>(SelectionMode.SINGLE)
    val list = DmList("l", source(members().sortedBy { it.team }), { id, model -> Label(id, model.map { it.name }) }, rowsPerPage = 6, selection = selection)
    list.sorts = listOf("Name" to "name", "Rate" to "rate")
    list.groupBy = { it.team }
    render(list)
    assertHooks(list)
    assertThat(tags("items")).hasSize(6)
    assertThat(tags("group")).hasSize(2)
    assertThat(tags("sort")).hasSize(2)
    assertThat(tags("select")).hasSize(6)
    assertStyleSheet("dm-list")

    tester.clickLink("l:items:1:select")
    assertThat(selection.single?.name).isEqualTo("Mia")
    assertThat(tags("items").count { it.getAttribute("aria-selected") == "true" }).isEqualTo(1)

    tester.clickLink("l:sorts:0:sort")
    assertThat(tags("sort")[0].getAttribute("aria-sort")).isEqualTo("ascending")
    assertThat(tag("item", 0).value).isEqualTo("Anna")

    tester.clickLink("l:pager:navigator:next")
    assertThat(list.dataView.currentPage).isEqualTo(1)
  }

  @Test
  fun `empty source shows the message and no items`() {
    render(DmList("l", ListDataSource<Member, String>(emptyList()), { id, model -> Label(id, model.map { it.name }) }))
    assertThat(tag("empty").value).isEqualTo("Nothing to show")
    assertThat(tags("items")).isEmpty()
    assertThat(tester.getTagByWicketId("sorts")).isNull()
  }
}
