package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmWindowTest : WidgetTestBase() {

  @Test
  fun `an embedded window is a region that is always open, without close link or script`() {
    val window = render(DmWindow<String>("w", Model.of("Details"), WindowMode.EMBEDDED, TextPanel(DmWindow.CONTENT, "body")))
    assertHooks(window)
    assertThat(window.isOpen).isTrue()
    assertThat(tag("dialog").name).isEqualTo("section")
    assertThat(tag("dialog").getAttribute("role")).isEqualTo("region")
    assertThat(tag("dialog").getAttribute("aria-labelledby")).isEqualTo(tag("title").getAttribute("id"))
    assertThat(tester.getTagByWicketId("close")).isNull()
    assertThat(tester.getTagByWicketId("footer")).isNull()
    assertThat(html).contains("body").doesNotContain("js/dm-window")
    assertStyleSheet("dm-window")
  }

  @Test
  fun `a modal window renders closed, opens by the server, closes with a result through the link`() {
    var reported: String? = "untouched"
    val window = DmWindow<String>("w", Model.of("Choose"), WindowMode.MODAL, TextPanel(DmWindow.CONTENT, "body"))
      .action { id -> object : Link<Void>(id) { override fun onClick() {} }.apply { setBody(Model.of("OK")) } }
    window.onClose = { reported = it }
    render(window)
    assertThat(tag("dialog").name).isEqualTo("dialog")
    assertThat(tag("dialog").getAttribute("open")).isNull()
    assertThat(tag("dialog").getAttribute("aria-modal")).isEqualTo("true")
    assertThat(tag("dialog").getAttribute("data-dm-window-draggable")).isNotNull
    assertThat(tag("dialog").getAttribute("class")).contains("dm-window-resizable")
    assertThat(html).contains("OK")
    assertScript("dm-window")

    window.open()
    renderAgain()
    assertThat(tag("dialog").getAttribute("open")).isNotNull

    window.result.`object` = "yes"
    tester.clickLink("w:dialog:close")
    assertThat(window.isOpen).isFalse()
    assertThat(window.result.`object`).isNull()
    assertThat(reported).isNull()
    assertThat(tag("dialog").getAttribute("open")).isNull()

    window.close(null, "saved")
    assertThat(reported).isEqualTo("saved")
    assertThat(window.result.`object`).isEqualTo("saved")
  }

  @Test
  fun `a floating window is a non-modal dialog`() {
    val window = render(DmWindow<String>("w", Model.of("Notes"), WindowMode.FLOATING, TextPanel(DmWindow.CONTENT, "body")).apply { draggable = false; resizable = false })
    window.open()
    renderAgain()
    assertThat(tag("dialog").getAttribute("aria-modal")).isNull()
    assertThat(tag("dialog").getAttribute("data-dm-window-draggable")).isNull()
    assertThat(tag("dialog").getAttribute("class")).doesNotContain("dm-window-resizable")
  }
}
