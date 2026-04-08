package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.MonthChoice
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MonthChoiceTest : WicketTestBase() {

  @Test
  fun `contains month values`() {
    val choice = MonthChoice("mc", Model.of(1))

    assertThat(choice.choices).contains(1)
  }

  @Test
  fun `contains all 12 months`() {
    val choice = MonthChoice("mc", Model.of(1))

    assertThat(choice.choices).hasSize(12)
  }

  @Test
  fun `choices are 1 through 12`() {
    val choice = MonthChoice("mc", Model.of(1))

    assertThat(choice.choices).containsAll((1..12).toList())
  }

  @Test
  fun `choice renderer displays month names`() {
    val choice = MonthChoice("mc", Model.of(1))

    invokeDeclared(choice, "onInitialize")

    assertThat(choice.choiceRenderer).isNotNull
  }
}
