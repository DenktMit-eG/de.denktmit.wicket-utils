package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.layout.Orientation
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmSliderTest : InputTestBase() {

  @Test
  fun `slider is a range input with the value shown and marks in a datalist`() {
    val model = Model<Double?>(40.0)
    val slider = renderInForm(DmSlider("level", model, 0.0, 100.0, 10.0, listOf(SliderMark(0.0, "none"), SliderMark(50.0), SliderMark(100.0, "all")), Orientation.VERTICAL))
    assertHooks(slider)
    assertThat(tag("input").getAttribute("type")).isEqualTo("range")
    assertThat(tag("input").getAttribute("min")).isEqualTo("0")
    assertThat(tag("input").getAttribute("max")).isEqualTo("100")
    assertThat(tag("input").getAttribute("step")).isEqualTo("10")
    assertThat(tag("input").getAttribute("orient")).isEqualTo("vertical")
    assertThat(tag("input").getAttribute("list")).isEqualTo(tag("marks").getAttribute("id"))
    assertThat(tag("value").value).isEqualTo("40")
    assertThat(tags("mark")).hasSize(3)
    assertThat(tag("mark", 0).getAttribute("label")).isEqualTo("none")
    assertStyleSheet("dm-slider")
    assertScript("dm-slider")
    form().apply { setValue("level:input", "70"); submit() }
    assertThat(model.`object`).isEqualTo(70.0)
  }

  @Test
  fun `range slider submits two values and keeps them in order`() {
    val model = Model<SliderRange?>(SliderRange(20.0, 60.0))
    val slider = renderInForm(DmRangeSlider("span", model, 0.0, 100.0, 5.0))
    assertHooks(slider)
    assertThat(tag("low").getAttribute("value")).isEqualTo("20")
    assertThat(tag("high").getAttribute("value")).isEqualTo("60")
    assertThat(tag("value").value).isEqualTo("20 – 60")
    form().apply { setValue("span:low", "80"); setValue("span:high", "30"); submit() }
    assertThat(model.`object`).isEqualTo(SliderRange(30.0, 80.0))
  }
}
