package de.denktmit.wicket.components

import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmAjaxFallbackButton
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import java.util.Optional

class DmAjaxFallbackButtonTest : WicketTestBase() {

  @Test
  fun `constructs with id and form`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f"))

    assertThat(button.id).isEqualTo("af")
  }

  @Test
  fun `submit and error callbacks receive optional target`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f"))
    var submitted = false
    var errored = false
    button.onSubmit = { submitted = it == null }
    button.execOnError = { errored = it == null }

    invokeDeclared(button, "onSubmit", Optional.empty<Any>())
    invokeDeclared(button, "onError", Optional.empty<Any>())

    assertThat(submitted).isTrue()
    assertThat(errored).isTrue()
  }

  @Test
  fun `onInitialize adds CSS`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f"))

    invokeDeclared(button, "onInitialize")

    assertThat(button.behaviors).isNotEmpty
  }

  @Test
  fun `null onSubmit does not throw`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f"))

    assertThatCode {
      invokeDeclared(button, "onSubmit", Optional.empty<Any>())
    }.doesNotThrowAnyException()
  }

  @Test
  fun `null execOnError does not throw`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f"))

    assertThatCode {
      invokeDeclared(button, "onError", Optional.empty<Any>())
    }.doesNotThrowAnyException()
  }

  @Test
  fun `visible callback`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f"))
    button.visible = { false }

    assertThat(button.visible != null).isTrue()
  }

  @Test
  fun `operator unaryPlus for Component`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f")) {
      +Label("child", "x")
    }

    invokeDeclared(button, "onInitialize")

    assertThat(button.get("child")).isNotNull
  }

  @Test
  fun `operator unaryPlus for Behavior`() {
    val button = DmAjaxFallbackButton("af", Form<Void>("f")) {
      +DisabledBehavior()
    }

    invokeDeclared(button, "onInitialize")

    assertThat(button.behaviors.filterIsInstance<DisabledBehavior>()).isNotEmpty
  }
}
