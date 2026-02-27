package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmDropDownChoiceRenderer
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmDropDownChoiceRendererTest : WicketTestBase() {

  @Test
  fun `constructs with renderer and choices`() {
    val choice = DmDropDownChoiceRenderer("dcr", Model.of("a"), Model.ofList(listOf("a")), ChoiceRenderer<String>())

    assertThat(choice.choices).containsExactly("a")
  }
}
