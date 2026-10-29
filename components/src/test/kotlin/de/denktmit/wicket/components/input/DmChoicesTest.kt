package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.apache.wicket.model.util.ListModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmChoicesTest : InputTestBase() {

  private val choices = ListModel(listOf("red", "green", "blue"))

  @Test
  fun `multi select submits the chosen values`() {
    val model = ListModel(mutableListOf("red"))
    val select = renderInForm(DmMultiSelect("colours", model, choices, rows = 3), "<select wicket:id=\"colours\"></select>")
    assertHooks(select)
    assertThat(tag("colours").getAttribute("multiple")).isNotNull
    assertThat(tag("colours").getAttribute("size")).isEqualTo("3")
    form().apply { selectMultiple("colours", intArrayOf(1, 2), true); submit() }
    assertThat(model.`object`).containsExactly("green", "blue")
  }

  @Test
  fun `check group renders one box per choice and submits a collection`() {
    val model = ListModel(mutableListOf<String>())
    val group = renderInForm(DmCheckGroup("colours", model, choices), "<span wicket:id=\"colours\"></span>")
    assertHooks(group)
    assertThat(html).containsPattern("type=\"checkbox\"[^>]*value=\"0\"")
    assertThat(tags("colours")).hasSize(1)
    form().apply { selectMultiple("colours", intArrayOf(0, 2)); submit() }
    assertThat(model.`object`).containsExactly("red", "blue")
  }

  @Test
  fun `radio choice with an empty choice`() {
    val model = Model<String?>("green")
    val radios = renderInForm(DmRadioChoice("colour", model, listOf("red", "green", "blue"), emptyChoice = "none"), "<span wicket:id=\"colour\"></span>")
    assertHooks(radios)
    assertThat(html).contains("none")
    assertThat(html).containsPattern("type=\"radio\"[^>]*checked[^>]*value=\"1\"")
    form().apply { select("colour", 0); submit() }
    assertThat(model.`object`).isEqualTo("red")
  }
}
