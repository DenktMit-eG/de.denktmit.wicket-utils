package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmIconTest : WidgetTestBase() {

  @Test
  fun `a decorative icon references the sprite symbol and hides from assistive technology`() {
    val icon = render(DmIcon("i", "close"), "<span wicket:id=\"i\"></span>")
    assertHooks(icon)
    val tag = tag("i")
    assertThat(tag.name).isEqualTo("svg")
    assertThat(tag.getAttribute("aria-hidden")).isEqualTo("true")
    assertThat(tag.getAttribute("class")).contains("icon")
    assertThat(html).containsPattern("<use href=\"[^\"]*dm-icons(-ver-[0-9]+)?.svg#close\"></use>")
  }

  @Test
  fun `a labelled icon is announced as an image`() {
    render(DmIcon("i", "warning", "Warning"), "<span wicket:id=\"i\"></span>")
    assertThat(tag("i").getAttribute("role")).isEqualTo("img")
    assertThat(tag("i").getAttribute("aria-label")).isEqualTo("Warning")
    assertThat(tag("i").getAttribute("aria-hidden")).isNull()
  }
}
