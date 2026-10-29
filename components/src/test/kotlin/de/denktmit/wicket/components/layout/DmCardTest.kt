package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.Link
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class DmCardTest : WidgetTestBase() {

  private val body = "<div wicket:id=\"card\"><p wicket:id=\"inner\"></p></div>"

  @Test
  fun `a plain card has header, title, body and no buttons or script`() {
    val card = render(DmCard("card", "Jobs") { +Label("inner", "running") }, body)
    assertHooks(card)
    assertThat(tag("shell").name).isEqualTo("section")
    assertThat(tag("header").name).isEqualTo("header")
    assertThat(tag("title").value).isEqualTo("Jobs")
    assertThat(tag("inner").value).isEqualTo("running")
    assertThat(tester.getTagByWicketId("maximize")).isNull()
    assertThat(tester.getTagByWicketId("close")).isNull()
    assertThat(tester.getTagByWicketId("footer")).isNull()
    assertThat(html).doesNotContain("js/dm-card")
    assertStyleSheet("dm-card")
  }

  @Test
  fun `actions, footer, collapse, maximise and close`() {
    val card = DmCard("card", "Jobs", collapsible = true, maximisable = true, closable = true)
      .action { id -> object : Link<Void>(id) { override fun onClick() {} }.apply { setBody(org.apache.wicket.model.Model.of("Refresh")) } }
      .footer(Label(DmCard.FOOTER, "3 jobs"))
    card.add(Label("inner", "running"))
    render(card, body)
    assertThat(tag("shell").name).isEqualTo("details")
    assertThat(tag("shell").getAttribute("open")).isNotNull
    assertThat(tag("header").name).isEqualTo("summary")
    assertThat(html).contains("Refresh")
    assertThat(tag("footer").value).isEqualTo("3 jobs")
    assertThat(tag("maximize").getAttribute("aria-label")).isEqualTo("Maximise")
    assertThat(tag("maximize").getAttribute("hidden")).isNotNull
    assertThat(tag("close").getAttribute("aria-label")).isEqualTo("Close")
    assertScript("dm-card")
  }

  @Test
  fun `a footer with another id is refused`() {
    assertThatThrownBy { DmCard("card", "x").footer(Label("other", "x")) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
