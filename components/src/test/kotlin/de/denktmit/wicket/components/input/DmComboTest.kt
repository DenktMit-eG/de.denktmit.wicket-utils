package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmComboTest : InputTestBase() {

  private val skills = listOf("Kotlin", "Kubernetes", "Keycloak", "Java")

  @Test
  fun `renders as a text input, answers suggestions from the source and submits free text`() {
    val model = Model<String?>("")
    val combo = renderInForm(DmCombo("skill", model, { text -> skills.filter { it.startsWith(text, ignoreCase = true) } }, minLength = 2, delay = 100, limit = 2), "<input wicket:id=\"skill\" type=\"text\"/>")
    assertHooks(combo)
    assertThat(tag("skill").getAttribute("type")).isEqualTo("text")
    assertThat(combo.getChoices("k").asSequence().toList()).containsExactly("Kotlin", "Kubernetes")
    form().apply { setValue("skill", "Rust"); submit() }
    assertThat(model.`object`).isEqualTo("Rust")
  }

  @Test
  fun `without free text a value the source does not offer is rejected`() {
    val model = Model<String?>("")
    renderInForm(DmCombo("skill", model, { text -> skills.filter { it.equals(text, ignoreCase = true) } }, freeText = false), "<input wicket:id=\"skill\" type=\"text\"/>")
    form().apply { setValue("skill", "Rust"); submit() }
    assertThat(model.`object`).isEqualTo("")
    tester.assertErrorMessages("'Rust' is not one of the offered values.")

    tester.clearFeedbackMessages()
    form().apply { setValue("skill", "Java"); submit() }
    assertThat(model.`object`).isEqualTo("Java")
  }
}
