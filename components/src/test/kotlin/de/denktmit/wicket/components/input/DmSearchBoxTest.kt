package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.apache.wicket.model.util.ListModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmSearchBoxTest : InputTestBase() {

  @Test
  fun `search input with recent values, search runs the action, clear empties the value`() {
    val model = Model<String?>("kotlin")
    var searched: String? = "untouched"
    val box = renderInForm(DmSearchBox("q", model, ListModel(listOf("kotlin", "wicket"))).apply { onSearch = { searched = it } })
    assertHooks(box)
    assertThat(tag("q").getAttribute("role")).isEqualTo("search")
    assertThat(tag("query").getAttribute("type")).isEqualTo("search")
    assertThat(tag("query").getAttribute("value")).isEqualTo("kotlin")
    assertThat(tag("query").getAttribute("list")).isEqualTo(tag("recent").getAttribute("id"))
    assertThat(tags("option").map { it.getAttribute("value") }).containsExactly("kotlin", "wicket")
    assertStyleSheet("dm-search-box")

    form().apply { setValue("q:query", "  java "); submit("q:search") }
    assertThat(model.`object`).isEqualTo("java")
    assertThat(searched).isEqualTo("java")

    form().submit("q:clear")
    assertThat(model.`object`).isNull()
    assertThat(searched).isNull()
  }

  @Test
  fun `no datalist without recent values`() {
    renderInForm(DmSearchBox("q", Model(null)))
    assertThat(tester.getTagByWicketId("recent")).isNull()
    assertThat(tag("query").getAttribute("list")).isNull()
  }
}
