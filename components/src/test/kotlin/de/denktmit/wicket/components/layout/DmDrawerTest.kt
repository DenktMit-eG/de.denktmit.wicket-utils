package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmDrawerTest : WidgetTestBase() {

  @Test
  fun `renders closed at its side, opens by the server and closes through the link`() {
    var closed = false
    val drawer = DmDrawer("d", "Filters", Side.START, TextPanel(DmDrawer.CONTENT, "filter form")).apply { onClose = { closed = true } }
    render(drawer)
    assertHooks(drawer)
    assertThat(tag("dialog").name).isEqualTo("dialog")
    assertThat(tag("dialog").getAttribute("open")).isNull()
    assertThat(tag("dialog").getAttribute("data-dm-drawer")).isEqualTo("start")
    assertThat(tag("dialog").getAttribute("class")).contains("dm-drawer-start")
    assertThat(tag("dialog").getAttribute("aria-labelledby")).isEqualTo(tag("title").getAttribute("id"))
    assertThat(html).contains("filter form")
    assertStyleSheet("dm-drawer")
    assertScript("dm-drawer")

    drawer.open()
    renderAgain()
    assertThat(tag("dialog").getAttribute("open")).isNotNull

    tester.clickLink("d:dialog:close")
    assertThat(drawer.isOpen).isFalse()
    assertThat(closed).isTrue()
  }
}
