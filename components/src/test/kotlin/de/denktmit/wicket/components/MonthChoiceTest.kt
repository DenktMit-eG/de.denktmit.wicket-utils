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
}
