package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmCenterTest : WidgetTestBase() {

  @Test
  fun `centre renders the primitive and the vertical variant`() {
    val centre = render(DmCenter("c", vertical = true, minHeight = "10rem", measure = "40ch"))
    assertHooks(centre)
    assertThat(tag("c").getAttribute("class")).contains("center", "center-intrinsic", "dm-center-vertical")
    assertThat(tag("c").getAttribute("style")).contains("--measure-wide:40ch", "--min-height:10rem")
    assertStyleSheet("dm-center")
  }

  @Test
  fun `a horizontal centre has no vertical class`() {
    render(DmCenter("c"))
    assertThat(tag("c").getAttribute("class")).doesNotContain("dm-center-vertical")
  }

  @Test
  fun `fit stretches its child`() {
    val fit = render(DmFit("f"))
    assertHooks(fit)
    assertThat(tag("f").getAttribute("class")).contains("dm-fit")
  }
}
