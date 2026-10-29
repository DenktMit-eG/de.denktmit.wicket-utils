package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.session.DmSession
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.Session
import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.apache.wicket.util.tester.TagTester
import org.apache.wicket.util.tester.WicketTester
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Renders widgets with a `WicketTester` and asserts what every widget of the catalogue promises: the stable hooks
 * (A.3), the header contributions (A.7, K.105) and the accessibility attributes (A.8).
 */
abstract class WidgetTestBase {

  class TestHomePage : WebPage()

  class WidgetTestApplication : WebApplication() {
    override fun getHomePage(): Class<out Page> = TestHomePage::class.java

    override fun newSession(request: Request, response: Response): Session = DmSession(request)
  }

  protected lateinit var tester: WicketTester

  @BeforeEach
  fun setUpWicket() {
    tester = WicketTester(WidgetTestApplication())
  }

  @AfterEach
  fun tearDownWicket() {
    tester.destroy()
  }

  /** A page that hosts one component in the markup the test gives it, so that head contributions render too. */
  class HostPage(component: Component, private val body: String) : WebPage(), IMarkupResourceStreamProvider {
    init {
      add(component)
    }

    override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream =
      StringResourceStream("<html xmlns:wicket=\"http://wicket.apache.org\"><head></head><body>$body</body></html>")
  }

  /** Renders [component] inside a page whose body is [body] (default: a `div` with the component's id). */
  protected fun <C : Component> render(component: C, body: String = "<div wicket:id=\"${component.id}\"></div>"): C {
    tester.startPage(HostPage(component, body))
    return component
  }

  /** Renders the last page again, so that rules evaluate once more. */
  protected fun renderAgain() {
    tester.startPage(tester.lastRenderedPage)
  }

  /** The last response's markup. */
  protected val html: String get() = tester.lastResponseAsString

  /** The rendered tag of the component with [wicketId]; the [index]th one when the id repeats (list items). */
  protected fun tag(wicketId: String, index: Int = 0): TagTester =
    tags(wicketId).getOrNull(index) ?: error("no rendered tag with wicket:id '$wicketId' at index $index")

  /** Every rendered tag with [wicketId], in document order. */
  protected fun tags(wicketId: String): List<TagTester> = TagTester.createTagsByAttribute(html, "wicket:id", wicketId, false)

  /** Asserts the two stable hooks on the rendered tag of [component]. */
  protected fun assertHooks(component: Component) {
    val classes = tag(component.id).getAttribute("class")?.split(" ") ?: emptyList()
    assertThat(classes).contains(WidgetHooks.idCssClass(component), WidgetHooks.kindCssClass(component))
  }

  private fun indexOfResource(path: String): Int = Regex(Regex.escape(path) + "(-ver-[0-9]+)?\\.(css|js)").find(html)?.range?.first ?: -1

  /** Asserts that the page head links the style sheet named [name] (`css/<name>.css`). */
  protected fun assertStyleSheet(name: String) {
    assertThat(indexOfResource("css/$name")).describedAs("style sheet css/$name.css linked").isGreaterThanOrEqualTo(0)
  }

  /** Asserts that the page head links the script named [name] (`js/<name>.js`) after the core script. */
  protected fun assertScript(name: String) {
    assertThat(indexOfResource("js/dm-core")).describedAs("core script linked").isGreaterThanOrEqualTo(0)
    if (name == "dm-core") return
    assertThat(indexOfResource("js/$name")).describedAs("script js/$name.js after the core script").isGreaterThan(indexOfResource("js/dm-core"))
  }
}
