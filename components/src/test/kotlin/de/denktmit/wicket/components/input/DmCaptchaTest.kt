package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmCaptchaTest : InputTestBase() {

  @Test
  fun `a wrong answer fails and renews the challenge, the right answer passes`() {
    val model = Model(false)
    val captcha = renderInForm(DmCaptcha("check", model))
    assertHooks(captcha)
    val question = tag("question").value
    assertThat(question).matches("What is \\d \\+ \\d\\?")
    assertThat(tag("answer").getAttribute("autocomplete")).isEqualTo("off")
    assertStyleSheet("dm-captcha")

    form().apply { setValue("check:answer", "99"); submit() }
    assertThat(model.`object`).isFalse()
    tester.assertErrorMessages("That is not the right answer.")

    tester.clearFeedbackMessages()
    val (a, b) = Regex("What is (\\d) \\+ (\\d)\\?").find(tag("question").value)!!.destructured
    form().apply { setValue("check:answer", (a.toInt() + b.toInt()).toString()); submit() }
    assertThat(model.`object`).isTrue()
    tester.assertNoErrorMessage()
  }
}
