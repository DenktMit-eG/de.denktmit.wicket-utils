package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmGenericPanel
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmGenericPanelTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val panel = DmGenericPanel<String>("g")

    assertThat(panel.id).isEqualTo("g")
  }

  @Test
  fun `visibility and ajax flags are configurable`() {
    val panel = DmGenericPanel<String>("g")
    panel.visible = { false }
    panel.enableAjax()

    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isFalse()
    assertThat(panel.outputMarkupId).isTrue()
    assertThat(panel.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `operator unaryPlus for Component and Behavior`() {
    val panel = object : DmGenericPanel<String>("g", {
      +Label("child", "x")
      +DisabledBehavior()
    }) {}

    invokeDeclared(panel, "onInitialize")

    assertThat(panel.get("child")).isNotNull
    assertThat(panel.behaviors).anyMatch { it is DisabledBehavior }
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val panel = object : DmGenericPanel<String>("g", { initCalled = true }) {}

    invokeDeclared(panel, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with visible true keeps visible`() {
    val panel = DmGenericPanel<String>("g")
    panel.visible = { true }

    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isTrue()
  }
}
