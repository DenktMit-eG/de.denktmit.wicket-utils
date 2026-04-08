package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmBorder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmBorderTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val border = DmBorder("b")

    assertThat(border.id).isEqualTo("b")
  }

  @Test
  fun `onConfigure applies visibility and ajax flags`() {
    val border = DmBorder("b")
    border.visible = { false }
    border.enableAjax()

    invokeDeclared(border, "onConfigure")

    assertThat(border.isVisible).isFalse()
    assertThat(border.outputMarkupId).isTrue()
    assertThat(border.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `operator unaryPlus for Behavior`() {
    val border = DmBorder("b")
    border.add(DisabledBehavior())

    assertThat(border.behaviors.filterIsInstance<DisabledBehavior>()).isNotEmpty
  }

  @Test
  fun `addComponents is invoked during onInitialize`() {
    var called = false
    val border = object : DmBorder("b") {
      override fun addComponents() {
        called = true
      }
    }

    invokeDeclared(border, "onInitialize")

    assertThat(called).isTrue()
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val border = DmBorder("b") { initCalled = true }

    invokeDeclared(border, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with visible true keeps visible`() {
    val border = DmBorder("b")
    border.visible = { true }

    invokeDeclared(border, "onConfigure")

    assertThat(border.isVisible).isTrue()
  }
}
