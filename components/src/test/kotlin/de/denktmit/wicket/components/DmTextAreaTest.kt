package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmTextArea
import io.mockk.mockk
import org.apache.wicket.behavior.AbstractAjaxBehavior
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTextAreaTest : WicketTestBase() {

  private class Holder(var text: String? = "x")

  @Test
  fun `constructs with id`() {
    val area = DmTextArea("ta", Model.of("x"))

    assertThat(area.id).isEqualTo("ta")
  }

  @Test
  fun `onInitialize adds CSS classes`() {
    val area = DmTextArea("ta", Model.of("x"))
    invokeDeclared(area, "onInitialize")

    assertThat(area.behaviors).isNotEmpty
  }

  @Test
  fun `onModelChanged invokes onChange callback`() {
    var changed: String? = null
    val area = DmTextArea("ta", Model.of("x"))
    area.onChange = { changed = it }

    invokeDeclared(area, "onModelChanged")

    assertThat(changed).isEqualTo("x")
  }

  @Test
  fun `enableAjax sets output flags`() {
    val area = DmTextArea("ta", Model.of("x"))
    area.enableAjax()

    assertThat(area.outputMarkupId).isTrue()
    assertThat(area.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `secondary KProperty constructor`() {
    val holder = Holder()
    val area = DmTextArea(holder::text)

    assertThat(area.id).isEqualTo("text")
  }

  @Test
  fun `operator unaryPlus for AbstractAjaxBehavior`() {
    val behavior = mockk<AbstractAjaxBehavior>(relaxed = true)
    val area = DmTextArea("ta", Model.of("x")) {
      +behavior
    }

    invokeDeclared(area, "onInitialize")

    assertThat(area.behaviors).contains(behavior)
  }

  @Test
  fun `init lambda is exercised during onInitialize`() {
    var initCalled = false
    val area = DmTextArea("ta", Model.of("x")) {
      initCalled = true
    }

    invokeDeclared(area, "onInitialize")

    assertThat(initCalled).isTrue()
  }
}
