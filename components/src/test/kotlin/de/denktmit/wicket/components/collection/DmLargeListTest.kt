package de.denktmit.wicket.components.collection

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmLargeListTest : WidgetTestBase() {

  @Test
  fun `renders the first batch and appends the next through the link until nothing is left`() {
    val list = render(DmLargeList("big", source(), { id, model -> Label(id, model.map { it.name }) }, pageSize = 5))
    assertHooks(list)
    assertThat(tags("items")).hasSize(5)
    assertThat(tag("moreLabel").value).isEqualTo("Load more (7 left)")
    assertStyleSheet("dm-list")
    assertScript("dm-large-list")

    tester.clickLink("big:more")
    assertThat(tags("items")).hasSize(10)
    assertThat(list.loaded).isEqualTo(10)
    tester.clickLink("big:more")
    assertThat(tags("items")).hasSize(12)
    assertThat(list.hasMore()).isFalse()
    assertThat(tester.getTagByWicketId("more")).isNull()
  }
}
