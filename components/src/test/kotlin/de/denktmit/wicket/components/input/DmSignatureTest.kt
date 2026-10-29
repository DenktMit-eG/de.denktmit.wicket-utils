package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmSignatureTest : InputTestBase() {

  @Test
  fun `a canvas with a hidden field that submits the image data url`() {
    val model = Model<String?>(null)
    val signature = renderInForm(DmSignature("sig", model, 300, 100))
    assertHooks(signature)
    assertThat(tag("sig").getAttribute("data-dm-signature-width")).isEqualTo("300")
    assertThat(html).contains("<canvas")
    assertThat(tag("image").getAttribute("type")).isEqualTo("hidden")
    assertStyleSheet("dm-signature")
    assertScript("dm-signature")
    form().apply { setValue("sig:image", "data:image/png;base64,AAAA"); submit() }
    assertThat(model.`object`).isEqualTo("data:image/png;base64,AAAA")

    form().apply { setValue("sig:image", "not an image"); submit() }
    assertThat(model.`object`).isNull()
  }
}
