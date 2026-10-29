package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class DmIncludeTest : WidgetTestBase() {

  @Test
  fun `shows nothing until content is chosen`() {
    val include = render(DmInclude("inc"))
    assertHooks(include)
    assertThat(tester.getTagByWicketId("content")).isNull()
    assertThat(tester.getTagByWicketId("page")).isNull()
  }

  @Test
  fun `shows a component chosen at runtime and swaps it`() {
    val include = DmInclude("inc").content(Label(DmInclude.CONTENT, "first"))
    render(include)
    assertThat(tag("content").value).isEqualTo("first")

    include.content(Label(DmInclude.CONTENT, "second"))
    renderAgain()
    assertThat(tag("content").value).isEqualTo("second")
  }

  @Test
  fun `shows a page in an inline frame`() {
    render(DmInclude("inc").page(TestHomePage::class.java))
    assertThat(tag("page").name).isEqualTo("iframe")
    assertThat(tag("page").getAttribute("src")).isNotBlank()
    assertThat(tester.getTagByWicketId("content")).isNull()
  }

  @Test
  fun `a content component with another id is refused`() {
    assertThatThrownBy { DmInclude("inc").content(Label("other", "x")) }.isInstanceOf(IllegalArgumentException::class.java)
  }
}
