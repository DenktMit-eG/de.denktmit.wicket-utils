package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmTextArea
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTextAreaTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val area = DmTextArea("ta", Model.of("x"))

    assertThat(area.id).isEqualTo("ta")
  }
}
