package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.layout.Orientation
import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DisplayWidgetsTest : WidgetTestBase() {

  @Test
  fun `text escapes, formats, breaks lines, links and hides when empty`() {
    val text = render(DmText("t", Model.of("see https://denktmit.de <now>\nsecond"), multiLine = true, linkify = true), "<p wicket:id=\"t\"></p>")
    assertHooks(text)
    assertThat(tag("t").value).isEqualTo("see <a href=\"https://denktmit.de\" rel=\"noopener\">https://denktmit.de</a> &lt;now&gt;<br>second")
    assertStyleSheet("dm-display")

    render(DmText("t", Model.of(42), format = { "no. $it" }, preformatted = true), "<p wicket:id=\"t\"></p>")
    assertThat(tag("t").name).isEqualTo("pre")
    assertThat(tag("t").value).isEqualTo("no. 42")

    render(DmText("t", Model.of("  "), hideWhenEmpty = true), "<p wicket:id=\"t\"></p>")
    assertThat(tester.getTagByWicketId("t")).isNull()
  }

  @Test
  fun `html is trusted, sanitised or escaped`() {
    val markup = "<p onclick=\"x\">Hi <b>there</b></p><script>bad()</script>"
    render(DmHtml("h", Model.of(markup), HtmlTrust.TRUSTED), "<div wicket:id=\"h\"></div>")
    assertThat(tag("h").value).isEqualTo(markup)
    render(DmHtml("h", Model.of(markup)), "<div wicket:id=\"h\"></div>")
    assertThat(tag("h").value).isEqualTo("<p>Hi <b>there</b></p>")
    render(DmHtml("h", Model.of(markup), HtmlTrust.ESCAPED), "<div wicket:id=\"h\"></div>")
    assertThat(tag("h").value).startsWith("&lt;p")
  }

  @Test
  fun `image map renders the image with areas that link`() {
    val map = render(DmImageMap("m", "/img/team.png", "The team", listOf(ImageArea("rect", "0,0,10,10", "Anna", href = "/anna"), ImageArea("circle", "20,20,5", "Home", page = TestHomePage::class.java))))
    assertHooks(map)
    assertThat(tag("image").getAttribute("src")).isEqualTo("/img/team.png")
    assertThat(tag("image").getAttribute("alt")).isEqualTo("The team")
    assertThat(tag("image").getAttribute("usemap")).isEqualTo("#" + tag("map").getAttribute("name"))
    assertThat(tags("areas")).hasSize(2)
    assertThat(tags("areas")[0].getAttribute("href")).isEqualTo("/anna")
    assertThat(tags("areas")[1].getAttribute("href")).isNotBlank()
    assertThat(tags("areas")[1].getAttribute("alt")).isEqualTo("Home")

    render(DmImageMap("m", "/img/one.png", "One"))
    assertThat(tester.getTagByWicketId("map")).isNull()
    assertThat(tag("image").getAttribute("usemap")).isNull()
  }

  @Test
  fun `progress is determinate with the value shown or indeterminate`() {
    val progress = render(DmProgress("p", Model.of(25.0)), "<div wicket:id=\"p\"></div>")
    assertHooks(progress)
    assertThat(tag("p").name).isEqualTo("progress")
    assertThat(tag("p").getAttribute("value")).isEqualTo("25.0")
    assertThat(tag("p").getAttribute("max")).isEqualTo("100.0")
    assertThat(tag("p").value).isEqualTo("25 %")
    render(DmProgress("p", Model<Double?>(null)), "<div wicket:id=\"p\"></div>")
    assertThat(tag("p").getAttribute("value")).isNull()
    assertThat(tag("p").getAttribute("class")).contains("dm-progress-indeterminate")
  }

  @Test
  fun `badge and tag carry a tone, a tag may be removed`() {
    val badge = render(DmBadge("b", "3", Tone.DANGER), "<span wicket:id=\"b\"></span>")
    assertHooks(badge)
    assertThat(tag("b").getAttribute("class")).contains("dm-tone-danger")
    assertThat(tag("b").value).isEqualTo("3")

    var removed = false
    val tag = render(DmTag("t", Model.of("Kotlin"), Tone.INFO, removable = true).apply { onRemove = { removed = true } })
    assertHooks(tag)
    assertThat(tag("text").value).isEqualTo("Kotlin")
    tester.clickLink("t:remove")
    assertThat(removed).isTrue()
    render(DmTag("t", Model.of("Java")))
    assertThat(tester.getTagByWicketId("remove")).isNull()
  }

  @Test
  fun `avatar shows the image or the initials and announces the name`() {
    val avatar = render(DmAvatar("a", "Anna Maria Adams", size = "3rem"))
    assertHooks(avatar)
    assertThat(avatar.initials()).isEqualTo("AA")
    assertThat(tag("a").getAttribute("role")).isEqualTo("img")
    assertThat(tag("a").getAttribute("aria-label")).isEqualTo("Anna Maria Adams")
    assertThat(tag("a").getAttribute("style")).contains("--avatar-size:3rem")
    assertThat(tag("initials").value).isEqualTo("AA")
    assertThat(tester.getTagByWicketId("image")).isNull()
    render(DmAvatar("a", "Ben", "/img/ben.png"))
    assertThat(tag("image").getAttribute("src")).isEqualTo("/img/ben.png")
    assertThat(tester.getTagByWicketId("initials")).isNull()
    assertThat(DmAvatar("a", "  ").initials()).isEqualTo("?")
    assertThat(DmAvatar("a", "cher").initials()).isEqualTo("C")
  }

  @Test
  fun `audio and video use the browser's controls`() {
    val audio = render(DmAudio("au", "/media/talk.mp3", "The talk"), "<div wicket:id=\"au\"></div>")
    assertHooks(audio)
    assertThat(tag("au").name).isEqualTo("audio")
    assertThat(tag("au").getAttribute("controls")).isNotNull
    assertThat(tag("au").getAttribute("src")).isEqualTo("/media/talk.mp3")
    assertThat(tag("au").getAttribute("aria-label")).isEqualTo("The talk")
    render(DmVideo("vi", "/media/demo.mp4"), "<div wicket:id=\"vi\"></div>")
    assertThat(tag("vi").name).isEqualTo("video")
  }

  @Test
  fun `empty state explains and offers the next action`() {
    val empty = render(DmEmptyState("e", "No members yet", "Add the first one").action(Label(DmEmptyState.ACTION, "Add member")))
    assertHooks(empty)
    assertThat(tag("title").value).isEqualTo("No members yet")
    assertThat(tag("text").value).isEqualTo("Add the first one")
    assertThat(tag("action").value).isEqualTo("Add member")
    render(DmEmptyState("e", "Nothing"))
    assertThat(tester.getTagByWicketId("text")).isNull()
    assertThat(tester.getTagByWicketId("action")).isNull()
  }

  @Test
  fun `organigram nests nodes, fisheye links icons`() {
    val org = render(DmOrganigram("o", OrgNode("Board", "leads", listOf(OrgNode("Platform", children = listOf(OrgNode("Anna"))), OrgNode("Product"))), Orientation.HORIZONTAL))
    assertHooks(org)
    assertThat(tag("o").getAttribute("class")).contains("dm-organigram-horizontal")
    assertThat(tags("name").map { it.value }).containsExactly("Board", "Platform", "Anna", "Product")
    assertThat(tags("role").map { it.value }).containsExactly("leads")
    assertStyleSheet("dm-organigram")

    val fisheye = render(DmFisheye("f", listOf(FisheyeItem("home", "Home", TestHomePage::class.java), FisheyeItem("user", "Members", TestHomePage::class.java))))
    assertHooks(fisheye)
    assertThat(tags("link")).hasSize(2)
    assertThat(tags("link")[0].getAttribute("aria-label")).isEqualTo("Home")
    tester.clickLink("f:items:1:link")
    tester.assertRenderedPage(TestHomePage::class.java)
  }
}
