package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmToolbarTest : WidgetTestBase() {

  @Test
  fun `renders actions, separators and spacers in a toolbar role with an overflow menu`() {
    val toolbar = DmToolbar("tb", Justify.END)
      .action { Label(it, "New") }
      .separator()
      .action { Label(it, "Delete") }
      .spacer()
      .action { Label(it, "Help") }
    render(toolbar)
    assertHooks(toolbar)
    assertThat(tag("tb").getAttribute("role")).isEqualTo("toolbar")
    assertThat(tag("tb").getAttribute("style")).contains("--justify:flex-end")
    assertThat(html).contains("New", "Delete", "Help")
    assertThat(html).contains("role=\"separator\"").contains("dm-toolbar-spacer")
    assertThat(html).contains("data-dm-toolbar-overflow")
    assertStyleSheet("dm-toolbar")
    assertScript("dm-toolbar")
  }
}
