package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmGroupBoxTest : WidgetTestBase() {

  private val body = "<div wicket:id=\"group\"><span wicket:id=\"inner\"></span></div>"

  @Test
  fun `a plain group box is a fieldset with a legend around the body`() {
    val box = render(DmGroupBox("group", "Address") { +Label("inner", "Street") }, body)
    assertHooks(box)
    assertThat(tag("shell").name).isEqualTo("fieldset")
    assertThat(tag("shell").getAttribute("open")).isNull()
    assertThat(tag("title").name).isEqualTo("legend")
    assertThat(tag("title").value).isEqualTo("Address")
    assertThat(tag("inner").value).isEqualTo("Street")
    assertStyleSheet("dm-group-box")
  }

  @Test
  fun `a collapsible group box is a details element that starts open or closed`() {
    render(DmGroupBox("group", "Address", collapsible = true, open = false) { +Label("inner", "Street") }, body)
    assertThat(tag("shell").name).isEqualTo("details")
    assertThat(tag("shell").getAttribute("open")).isNull()
    assertThat(tag("title").name).isEqualTo("summary")

    render(DmGroupBox("group", "Address", collapsible = true) { +Label("inner", "Street") }, body)
    assertThat(tag("shell").getAttribute("open")).isNotNull
  }
}
