package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.Side
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmSwitchTest : InputTestBase() {

  @Test
  fun `a switch is a check box with the switch role inside its label and submits without script`() {
    val model = Model(false)
    val switch = renderInForm(DmSwitch("notify", model, "Notify me", Side.START))
    assertHooks(switch)
    assertThat(tag("notify").getAttribute("class")).contains("dm-switch-label-start")
    assertThat(tag("input").getAttribute("type")).isEqualTo("checkbox")
    assertThat(tag("input").getAttribute("role")).isEqualTo("switch")
    assertThat(tag("input").getAttribute("aria-checked")).isEqualTo("false")
    assertThat(tag("label").value).isEqualTo("Notify me")
    assertStyleSheet("dm-switch")
    form().apply { setValue("notify:input", "true"); submit() }
    assertThat(model.`object`).isTrue()
  }

  @Test
  fun `tri-state reads the script's state when present and the box otherwise`() {
    val model = Model<Boolean?>(null)
    val box = renderInForm(DmTriStateCheckbox("all", model, "Select all"))
    assertHooks(box)
    assertThat(tag("input").getAttribute("aria-checked")).isEqualTo("mixed")
    assertThat(tag("input").getAttribute("data-dm-indeterminate")).isNotNull
    assertScript("dm-tri-state-checkbox")
    form().apply { setValue("all:input", "true"); submit() }
    assertThat(model.`object`).isTrue()

    form().apply { setValue("all:state", "null"); submit() }
    assertThat(model.`object`).isNull()
    form().apply { setValue("all:state", "false"); setValue("all:input", "true"); submit() }
    assertThat(model.`object`).isFalse()
  }
}
