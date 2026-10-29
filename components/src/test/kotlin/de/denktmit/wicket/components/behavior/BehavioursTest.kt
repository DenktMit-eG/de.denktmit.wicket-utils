package de.denktmit.wicket.components.behavior

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class BehavioursTest : WidgetTestBase() {

  @Test
  fun `a keyboard shortcut binds keys to a server action`() {
    var fired = 0
    val box = render(WebMarkupContainer("box").shortcut("Ctrl+S") { fired++ })
    assertThat(tag("box").getAttribute("data-dm-shortcut-keys")).isEqualTo("Ctrl+S")
    assertThat(tag("box").getAttribute("data-dm-shortcut-call")).contains("Wicket.Ajax")
    assertScript("dm-shortcut")
    tester.executeBehavior(box.behaviors.filterIsInstance<AbstractDefaultAjaxBehavior>().single())
    assertThat(fired).isEqualTo(1)
  }

  @Test
  fun `drag source and drop target report the dropped key`() {
    var dropped: String? = null
    val source = Label("s", "card").dragSource("card-1", "cards")
    val target = WebMarkupContainer("t").dropTarget("cards") { key, _ -> dropped = key }
    val box = WebMarkupContainer("box").apply { add(source, target) }
    render(box, "<div wicket:id=\"box\"><span wicket:id=\"s\"></span><div wicket:id=\"t\"></div></div>")
    assertThat(tag("s").getAttribute("draggable")).isEqualTo("true")
    assertThat(tag("s").getAttribute("data-dm-drag-key")).isEqualTo("card-1")
    assertThat(tag("t").getAttribute("data-dm-drop-group")).isEqualTo("cards")
    assertThat(tag("t").getAttribute("data-dm-drop-call")).contains("Wicket.Ajax")
    assertStyleSheet("dm-drag-drop")
    assertScript("dm-drag-drop")
    tester.request.setParameter("key", "card-1")
    tester.executeBehavior(target.behaviors.filterIsInstance<AbstractDefaultAjaxBehavior>().single())
    assertThat(dropped).isEqualTo("card-1")
  }

  @Test
  fun `scroll into view and focus append script to the ajax response`() {
    val region = Label("r", "region")
    val link = object : AjaxLink<Void>("l") {
      override fun onClick(target: AjaxRequestTarget) {
        target.scrollTo(region)
        target.focus(region)
      }
    }
    val box = WebMarkupContainer("box").apply { add(region, link) }
    render(box, "<div wicket:id=\"box\"><span wicket:id=\"r\"></span><a wicket:id=\"l\">go</a></div>")
    tester.clickLink("box:l", true)
    assertThat(tester.lastResponseAsString).contains("scrollIntoView").contains(region.markupId)
  }

  @Test
  fun `confirm marks the element with the question and brings the script`() {
    render(Label("l", "Delete").confirm("Really delete?"), "<a wicket:id=\"l\"></a>")
    assertThat(tag("l").getAttribute("data-dm-confirm")).isEqualTo("Really delete?")
    assertScript("dm-confirm")
  }

  @Test
  fun `refresh timer updates its region on every tick`() {
    var ticks = 0
    val region = Label("r", object : org.apache.wicket.model.IModel<String> { override fun getObject() = "tick $ticks" })
    region.add(RefreshTimer(Duration.ofSeconds(5)) { ticks++ })
    render(region, "<span wicket:id=\"r\"></span>")
    assertThat(tag("r").value).isEqualTo("tick 0")
    tester.executeBehavior(region.behaviors.filterIsInstance<RefreshTimer>().single())
    assertThat(ticks).isEqualTo(1)
    assertThat(tester.lastResponseAsString).contains("tick 1")
  }
}
