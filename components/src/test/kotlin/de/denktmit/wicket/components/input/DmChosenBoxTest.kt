package de.denktmit.wicket.components.input

import org.apache.wicket.model.util.ListModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmChosenBoxTest : InputTestBase() {

  @Test
  fun `the form reads a native multiple select, script data carries the placeholder`() {
    val model = ListModel(mutableListOf("Kotlin"))
    val box = renderInForm(DmChosenBox("skills", model, ListModel(listOf("Kotlin", "Java", "Rust"))))
    assertHooks(box)
    assertThat(tag("skills").getAttribute("data-dm-chosen-placeholder")).isEqualTo("Add")
    assertThat(tag("select").getAttribute("multiple")).isNotNull
    assertThat(html).containsPattern("<option selected=\"selected\" value=\"0\">Kotlin")
    assertStyleSheet("dm-chosen-box")
    assertScript("dm-chosen-box")
    form().apply { selectMultiple("skills:select", intArrayOf(1, 2), true); submit() }
    assertThat(model.`object`).containsExactly("Java", "Rust")
  }
}
