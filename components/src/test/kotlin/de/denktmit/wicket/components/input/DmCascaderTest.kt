package de.denktmit.wicket.components.input

import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.model.util.ListModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmCascaderTest : InputTestBase() {

  private val tree = mapOf<String?, List<String>>(null to listOf("DE", "FR"), "DE" to listOf("Berlin", "Bavaria"), "FR" to listOf("Ile-de-France"), "Bavaria" to listOf("Munich", "Nuremberg"), "Berlin" to listOf("Berlin"))

  private fun cascader(model: ListModel<String>) = DmCascader("place", model, 3, { tree[it] ?: emptyList() }, ChoiceRenderer<String>(), listOf("Country", "Region", "City"))

  @Test
  fun `renders only the levels that have a parent and submits the path`() {
    val model = ListModel(listOf("DE", "Bavaria"))
    val cascader = renderInForm(cascader(model))
    assertHooks(cascader)
    assertThat(tags("levels")).hasSize(3)
    assertThat(tag("levels", 0).getAttribute("aria-label")).isEqualTo("Country")
    assertThat(html).contains("Munich").contains("Nuremberg")
    assertStyleSheet("dm-cascader")

    form().apply { select("place:levels:3", 1); submit() }
    assertThat(model.`object`).containsExactly("DE", "Bavaria", "Nuremberg")
  }

  @Test
  fun `only the root level shows without a choice, a change by ajax reveals the next level`() {
    val model = ListModel(emptyList<String>())
    val cascader = renderInForm(cascader(model))
    assertThat(tags("levels")).hasSize(1)
    form().select("place:levels:1", 0)
    tester.executeAjaxEvent("form:place:levels:1", "change")
    assertThat(tags("levels")).hasSize(2)
    assertThat(html).contains("Berlin").contains("Bavaria")
    assertThat(cascader.select(0).modelObject).isEqualTo("DE")
  }
}
