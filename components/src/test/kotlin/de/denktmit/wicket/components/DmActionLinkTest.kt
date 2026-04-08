package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmActionLink
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class DmActionLinkTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val link = DmActionLink("a")

    assertThat(link.id).isEqualTo("a")
  }

  @Test
  fun `invokes click callback`() {
    var clicked = false
    val link = DmActionLink("x")
    link.onClick = { clicked = true }

    link.onClick()

    assertThat(clicked).isTrue()
  }

  @Test
  fun `configure applies visible callback`() {
    val link = DmActionLink("x")
    link.visible = { false }

    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isFalse()
  }

  @Test
  fun `onInitialize with bodyModel sets body`() {
    val link = DmActionLink("a", bodyModel = Model.of("text"))

    invokeDeclared(link, "onInitialize")

    assertThat(link.body.`object`).isEqualTo("text")
  }

  @Test
  fun `null onClick does not throw`() {
    val link = DmActionLink("a")

    assertThatCode {
      invokeDeclared(link, "onClick")
    }.doesNotThrowAnyException()
  }

  @Test
  fun `operator unaryPlus for Component and Behavior`() {
    val link = DmActionLink("a") {
      +Label("child", "x")
      +DisabledBehavior()
    }

    invokeDeclared(link, "onInitialize")

    assertThat(link.get("child")).isNotNull
    assertThat(link.behaviors.filterIsInstance<DisabledBehavior>()).isNotEmpty
  }

  @Test
  fun `onInitialize calls init lambda`() {
    var initCalled = false
    val link = DmActionLink("a") { initCalled = true }

    invokeDeclared(link, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `onConfigure with visible true keeps visible`() {
    val link = DmActionLink("a")
    link.visible = { true }

    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isTrue()
  }
}
