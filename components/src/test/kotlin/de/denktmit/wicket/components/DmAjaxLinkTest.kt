package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmAjaxLink
import io.mockk.mockk
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAjaxLinkTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val link = DmAjaxLink<Any>("al", click = {})

    assertThat(link.id).isEqualTo("al")
  }

  @Test
  fun `click handler and visible callback are used`() {
    var clicked = false
    val link =
      DmAjaxLink<Any>("al", click = {
        clicked = true
      })
    link.visible = { false }

    link.onClick(mockk<AjaxRequestTarget>(relaxed = true))
    invokeDeclared(link, "onConfigure")

    assertThat(clicked).isTrue()
    assertThat(link.isVisible).isFalse()
  }

  @Test
  fun `onInitialize with bodyModel sets body`() {
    val link = DmAjaxLink<Any>("al", click = {}, bodyModel = Model.of("text"))

    invokeDeclared(link, "onInitialize")

    assertThat(link.body.`object`).isEqualTo("text")
  }

  @Test
  fun `operator unaryPlus for Component and Behavior`() {
    val link = DmAjaxLink<Any>("al", click = {}) {
      +Label("child", "x")
      +DisabledBehavior()
    }

    invokeDeclared(link, "onInitialize")

    assertThat(link.get("child")).isNotNull
    assertThat(link.behaviors.filterIsInstance<DisabledBehavior>()).isNotEmpty
  }
}
