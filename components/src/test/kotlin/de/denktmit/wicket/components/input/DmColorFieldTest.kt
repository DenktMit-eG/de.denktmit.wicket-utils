package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmColorFieldTest : InputTestBase() {

  @Test
  fun `picker and text show the value, the text wins when valid, the picker otherwise`() {
    val model = Model<String?>("#13343d")
    val field = renderInForm(DmColorField("colour", model))
    assertHooks(field)
    assertThat(tag("picker").getAttribute("type")).isEqualTo("color")
    assertThat(tag("picker").getAttribute("value")).isEqualTo("#13343d")
    assertThat(tag("text").getAttribute("value")).isEqualTo("#13343d")
    assertThat(tag("text").getAttribute("pattern")).isEqualTo("#[0-9a-fA-F]{6}")
    assertScript("dm-color-field")

    form().apply { setValue("colour:text", "#FF6478"); setValue("colour:picker", "#000000"); submit() }
    assertThat(model.`object`).isEqualTo("#ff6478")

    form().apply { setValue("colour:text", "not a colour"); setValue("colour:picker", "#71d5da"); submit() }
    assertThat(model.`object`).isEqualTo("#71d5da")

    form().apply { setValue("colour:text", ""); submit() }
    assertThat(model.`object`).isNull()
  }
}
