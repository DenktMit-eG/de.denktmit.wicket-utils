package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmPanel
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPanelTest : WicketTestBase() {

  @Test
  fun `visibility and ajax flags are configurable`() {
    val panel = object : DmPanel("p") {}
    panel.visible = { false }
    panel.enableAjax()

    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isFalse()
    assertThat(panel.outputMarkupId).isTrue()
    assertThat(panel.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `addComponents is invoked during onInitialize`() {
    var added = false
    val panel = object : DmPanel("p") {
      override fun addComponents() {
        added = true
      }
    }

    invokeDeclared(panel, "onInitialize")

    assertThat(added).isTrue()
  }

  @Test
  fun `operator unaryPlus for Component`() {
    val panel = object : DmPanel("p") {
      override fun addComponents() {
        +Label("child", "x")
      }
    }

    invokeDeclared(panel, "onInitialize")

    assertThat(panel.get("child")).isNotNull
  }

  @Test
  fun `operator unaryPlus for Behavior`() {
    val panel = object : DmPanel("p") {
      override fun addComponents() {
        +DisabledBehavior()
      }
    }

    invokeDeclared(panel, "onInitialize")

    assertThat(panel.behaviors.filterIsInstance<DisabledBehavior>()).isNotEmpty
  }

  @Test
  fun `onConfigure without visible does not change visibility`() {
    val panel = object : DmPanel("p") {}

    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isTrue()
  }
}
