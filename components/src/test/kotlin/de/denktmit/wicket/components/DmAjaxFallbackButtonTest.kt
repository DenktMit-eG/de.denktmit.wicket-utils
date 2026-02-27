package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmAjaxFallbackButton
import org.apache.wicket.markup.html.form.Form
import org.assertj.core.api.Assertions.assertThat
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
}
