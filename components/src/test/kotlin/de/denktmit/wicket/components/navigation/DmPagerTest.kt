package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.navigation.paging.IPageableItems
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPagerTest : WidgetTestBase() {

  private class Items(private val count: Long) : IPageableItems {
    private var page = 0L
    private var perPage = 10L

    override fun getCurrentPage(): Long = page

    override fun setCurrentPage(p: Long) {
      page = p
    }

    override fun getPageCount(): Long = (count + perPage - 1) / perPage

    override fun getItemCount(): Long = count

    override fun getItemsPerPage(): Long = perPage

    override fun setItemsPerPage(n: Long) {
      perPage = n
    }
  }

  @Test
  fun `first, previous, numbers, next, last, total count and page sizes by link`() {
    val items = Items(95)
    val pager = render(DmPager("pager", items))
    assertHooks(pager)
    assertThat(tag("pager").getAttribute("role")).isEqualTo("navigation")
    assertThat(tag("total").value).isEqualTo("95 items")
    assertThat(tags("size")).hasSize(3)
    assertThat(tags("size")[0].getAttribute("aria-current")).isEqualTo("true")
    assertThat(tags("next")).hasSize(1)
    assertThat(tags("last")).hasSize(1)
    assertThat(tags("first")).hasSize(1)
    assertStyleSheet("dm-pager")

    tester.clickLink("pager:navigator:next")
    assertThat(items.currentPage).isEqualTo(1)
    tester.clickLink("pager:navigator:last")
    assertThat(items.currentPage).isEqualTo(9)
    tester.clickLink("pager:sizes:1:size")
    assertThat(items.itemsPerPage).isEqualTo(25)
    assertThat(items.currentPage).isEqualTo(0)
    assertThat(tags("size")[1].getAttribute("aria-current")).isEqualTo("true")
  }

  @Test
  fun `ajax pager refreshes itself and the collection`() {
    val items = Items(30)
    val collection = org.apache.wicket.markup.html.basic.Label("list", "rows")
    val pager = DmPager("pager", items, ajax = true, pageSizes = emptyList()).refresh(collection)
    val box = org.apache.wicket.markup.html.WebMarkupContainer("box").apply { add(collection, pager) }
    render(box, "<div wicket:id=\"box\"><span wicket:id=\"list\"></span><div wicket:id=\"pager\"></div></div>")
    assertThat(tester.getTagByWicketId("sizes")).isNull()
    tester.clickLink("box:pager:navigator:next", true)
    assertThat(items.currentPage).isEqualTo(1)
    assertThat(tester.lastResponseAsString).contains(collection.markupId)
  }
}
