package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.display.Tone
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.layout.Side
import de.denktmit.wicket.components.layout.TextPanel
import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OverlaysTest : WidgetTestBase() {

  @Test
  fun `notifications show once with tone, role and duration, then clear`() {
    val region = DmNotifications("n", Side.BOTTOM)
    region.notify("Saved", Tone.SUCCESS)
    region.notify(Notification("Failed", Tone.DANGER, 0))
    render(region)
    assertHooks(region)
    assertThat(tag("n").getAttribute("class")).contains("dm-notifications-bottom")
    assertThat(tag("n").getAttribute("aria-live")).isEqualTo("polite")
    assertThat(tags("items")).hasSize(2)
    assertThat(tags("items")[0].getAttribute("role")).isEqualTo("status")
    assertThat(tags("items")[0].getAttribute("data-dm-notification-duration")).isEqualTo("5000")
    assertThat(tags("items")[1].getAttribute("role")).isEqualTo("alert")
    assertThat(tags("items")[1].getAttribute("class")).contains("dm-tone-danger")
    assertStyleSheet("dm-notification")
    assertScript("dm-notification")
    assertThat(region.notifications).isEmpty()
    renderAgain()
    assertThat(tags("items")).isEmpty()
  }

  @Test
  fun `message box answers through its buttons and cancels on close`() {
    val answers = mutableListOf<String>()
    val box = DmMessageBox("mb", "Delete?", "This cannot be undone.", DmMessageBox.yesNo()).apply { onAnswer = { a, label, _ -> answers.add("$a:$label") } }
    render(box)
    assertHooks(box)
    assertThat(tester.getTagByWicketId("dialog")?.getAttribute("open")).isNull()
    assertThat(tag("question").value).isEqualTo("This cannot be undone.")
    assertThat(html).contains("Yes").contains("No")
    box.open()
    renderAgain()
    assertThat(tag("dialog").getAttribute("open")).isNotNull
    assertThat(box.isOpen).isTrue()

    tester.clickLink("mb:window:dialog:footer:actions:1")
    assertThat(box.answer).isEqualTo(Answer.YES)
    assertThat(answers).containsExactly("YES:Yes")
    assertThat(box.isOpen).isFalse()

    box.open()
    renderAgain()
    tester.clickLink("mb:window:dialog:close")
    assertThat(box.answer).isEqualTo(Answer.CANCEL)
    assertThat(answers).containsExactly("YES:Yes", "CANCEL:")
  }

  @Test
  fun `popup is a details element anchored to another widget`() {
    val owner = Label("owner", "hover me")
    val popup = DmPopup("p", TextPanel(DmPopup.CONTENT, "the panel"), trigger = PopupTrigger.HOVER).anchorTo(owner)
    val box = WebMarkupContainer("box").apply { add(owner, popup) }
    render(box, "<div wicket:id=\"box\"><span wicket:id=\"owner\"></span><div wicket:id=\"p\"></div></div>")
    assertHooks(popup)
    assertThat(tag("p").getAttribute("data-dm-popup")).isEqualTo("hover")
    assertThat(tag("p").getAttribute("data-dm-popup-for")).isEqualTo(tag("owner").getAttribute("id"))
    assertThat(tag("details").getAttribute("open")).isNull()
    assertThat(html).contains("the panel")
    assertStyleSheet("dm-popup")
    assertScript("dm-popup")
    popup.isOpen = true
    renderAgain()
    assertThat(tag("details").getAttribute("open")).isNotNull
  }

  @Test
  fun `tooltip keeps the title for script-less use and the data for script`() {
    val label = render(Label("l", "Rate").tooltip("Hourly rate in euro", 200), "<span wicket:id=\"l\"></span>")
    assertThat(tag("l").getAttribute("title")).isEqualTo("Hourly rate in euro")
    assertThat(tag("l").getAttribute("data-dm-tooltip")).isEqualTo("Hourly rate in euro")
    assertThat(tag("l").getAttribute("data-dm-tooltip-delay")).isEqualTo("200")
    assertStyleSheet("dm-tooltip")
    assertScript("dm-tooltip")
    assertThat(label.behaviors).anyMatch { it is TooltipBehavior }
  }

  @Test
  fun `coach mark steps through its sequence by link`() {
    val a = Label("a", "first")
    val b = Label("b", "second")
    var done = false
    val coach = DmCoachMark("c", listOf(CoachStep(a, "Step one", "This is a"), CoachStep(b, "Step two", "This is b"))).apply { onDone = { done = true } }
    val box = WebMarkupContainer("box").apply { add(a, b, coach) }
    render(box, "<div wicket:id=\"box\"><span wicket:id=\"a\"></span><span wicket:id=\"b\"></span><div wicket:id=\"c\"></div></div>")
    assertHooks(coach)
    assertThat(tester.getTagByWicketId("dialog")).isNull()
    coach.start()
    renderAgain()
    assertThat(tag("dialog").getAttribute("data-dm-coach-target")).isEqualTo(a.markupId)
    assertThat(tag("title").value).isEqualTo("Step one")
    assertThat(tag("position").value).isEqualTo("1 of 2")
    assertThat(tester.getTagByWicketId("done")).isNull()
    assertStyleSheet("dm-coach-mark")
    assertScript("dm-coach-mark")

    tester.clickLink("box:c:dialog:next")
    assertThat(coach.current).isEqualTo(1)
    assertThat(tag("dialog").getAttribute("data-dm-coach-target")).isEqualTo(b.markupId)
    assertThat(tester.getTagByWicketId("next")).isNull()
    tester.clickLink("box:c:dialog:done")
    assertThat(coach.isOpen).isFalse()
    assertThat(done).isTrue()
    assertThat(tester.getTagByWicketId("dialog")).isNull()
  }

  @Test
  fun `feedback panel renders messages with their level, hooks, sheet and dismiss script`() {
    val panel = DmFeedbackPanel("fb")
    val box = WebMarkupContainer("box").apply { add(panel) }
    render(box, "<div wicket:id=\"box\"><div wicket:id=\"fb\"></div></div>")
    assertThat(tester.getTagByWicketId("fb")).isNull()
    box.info("Saved")
    box.error("Broken")
    renderAgain()
    assertHooks(panel)
    assertThat(tag("fb").getAttribute("data-dm-feedback")).isNotNull
    assertThat(tags("messages").map { it.getAttribute("class") }).anyMatch { it.contains("alert-info") }.anyMatch { it.contains("alert-danger") }
    assertThat(tags("message").map { it.value }).containsExactly("Saved", "Broken")
    assertStyleSheet("dm-feedback")
    assertScript("dm-feedback")
  }
}
