package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmDropDownChoice
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmDropDownChoiceTest : WicketTestBase() {

  @Test
  fun `constructs with choices`() {
    val choice = DmDropDownChoice("dc", Model.of("a"), Model.ofList(listOf("a", "b")))

    assertThat(choice.choices).containsExactly("a", "b")
  }

  @Test
  fun `onConfigure applies visibility and enabled callbacks`() {
    val choice = DmDropDownChoice("dc", Model.of("a"), Model.ofList(listOf("a", "b")))
    choice.visible = { false }
    choice.enable = { false }

    invokeDeclared(choice, "onConfigure")

    assertThat(choice.isVisible).isFalse()
    assertThat(choice.isEnabled).isFalse()
  }

  @Test
  fun `onInitialize adds CSS`() {
    val choice = DmDropDownChoice("dc", Model.of("a"), Model.ofList(listOf("a", "b")))

    invokeDeclared(choice, "onInitialize")
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val choice = DmDropDownChoice("dc", Model.of("a"), Model.ofList(listOf("a", "b"))) { initCalled = true }

    invokeDeclared(choice, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with visible true keeps visible`() {
    val choice = DmDropDownChoice("dc", Model.of("a"), Model.ofList(listOf("a", "b")))
    choice.visible = { true }

    invokeDeclared(choice, "onConfigure")

    assertThat(choice.isVisible).isTrue()
  }

  @Test
  fun `onConfigure with enable true keeps enabled`() {
    val choice = DmDropDownChoice("dc", Model.of("a"), Model.ofList(listOf("a", "b")))
    choice.enable = { true }

    invokeDeclared(choice, "onConfigure")

    assertThat(choice.isEnabled).isTrue()
  }
}
