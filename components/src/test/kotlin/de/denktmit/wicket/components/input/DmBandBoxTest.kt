package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.TextPanel
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmBandBoxTest : InputTestBase() {

  private val members = mapOf("anna" to "Anna Adams", "ben" to "Ben Brown")

  private fun bandBox(model: Model<String?>) = DmBandBox("member", model, display = { members[it] ?: it }, key = { it }, fromKey = { members.keys.firstOrNull { k -> k == it } }, popup = { TextPanel(it, "a member list") })

  @Test
  fun `shows the display text, submits the key, and the popup is a details element with the author's panel`() {
    val model = Model<String?>("anna")
    val box = renderInForm(bandBox(model))
    assertHooks(box)
    assertThat(tag("display").getAttribute("value")).isEqualTo("Anna Adams")
    assertThat(tag("display").getAttribute("disabled")).isNotNull
    assertThat(tag("key").getAttribute("value")).isEqualTo("anna")
    assertThat(tag("popup").name).isEqualTo("details")
    assertThat(tag("popup").getAttribute("open")).isNull()
    assertThat(html).contains("a member list")
    assertStyleSheet("dm-band-box")
    assertScript("dm-band-box")
    form().apply { setValue("member:key", "ben"); submit() }
    assertThat(model.`object`).isEqualTo("ben")

    form().apply { setValue("member:key", "nobody"); submit() }
    assertThat(model.`object`).isNull()
  }

  @Test
  fun `choose sets the value and closes the popup`() {
    val model = Model<String?>(null)
    val box = renderInForm(bandBox(model))
    box.isOpen = true
    renderAgain()
    assertThat(tag("popup").getAttribute("open")).isNotNull
    box.choose("ben")
    renderAgain()
    assertThat(model.`object`).isEqualTo("ben")
    assertThat(tag("display").getAttribute("value")).isEqualTo("Ben Brown")
    assertThat(tag("popup").getAttribute("open")).isNull()
  }
}
