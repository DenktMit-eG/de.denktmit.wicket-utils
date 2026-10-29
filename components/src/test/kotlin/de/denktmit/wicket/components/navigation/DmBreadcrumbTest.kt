package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmBreadcrumbTest : WidgetTestBase() {

  @Test
  fun `links on every step but the last, which is the current page`() {
    val crumbs = render(DmBreadcrumb("bc", listOf(Crumb("Home", TestHomePage::class.java), Crumb("Members", TestHomePage::class.java), Crumb("Anna"))))
    assertHooks(crumbs)
    assertThat(tag("bc").getAttribute("aria-label")).isEqualTo("Breadcrumb")
    assertThat(tags("link")).hasSize(2)
    assertThat(tags("label")).hasSize(1)
    assertThat(tag("label").getAttribute("aria-current")).isEqualTo("page")
    assertThat(tag("label").value).isEqualTo("Anna")
    assertStyleSheet("dm-breadcrumb")
    tester.clickLink("bc:crumbs:0:link")
    tester.assertRenderedPage(TestHomePage::class.java)
  }

  @Test
  fun `a middle step without a page is plain text`() {
    render(DmBreadcrumb("bc", listOf(Crumb("Home", TestHomePage::class.java), Crumb("Group"), Crumb("Here"))))
    assertThat(tags("link")).hasSize(1)
    assertThat(tags("label").map { it.value }).containsExactly("Group", "Here")
  }
}
