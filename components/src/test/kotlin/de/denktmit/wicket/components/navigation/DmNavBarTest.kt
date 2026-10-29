package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.layout.Orientation
import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmNavBarTest : WidgetTestBase() {

  private val items = listOf(
    NavItem("Home", TestHomePage::class.java, icon = "home"),
    NavItem("Members", HostPage::class.java, icon = "user", badge = "3", children = listOf(NavItem("New", TestHomePage::class.java))),
  )

  @Test
  fun `vertical bar with icons, badges, sub-items and the current page marked`() {
    val bar = render(DmNavBar("nav", items))
    assertHooks(bar)
    assertThat(tag("nav").getAttribute("aria-label")).isEqualTo("Main")
    assertThat(tag("nav").getAttribute("class")).contains("dm-nav-bar-vertical")
    assertThat(tags("link")).hasSize(3)
    assertThat(tags("link")[1].getAttribute("aria-current")).isEqualTo("page")
    assertThat(tags("link")[0].getAttribute("aria-current")).isNull()
    assertThat(tag("badge").value).isEqualTo("3")
    assertThat(tags("children")).hasSize(1)
    assertStyleSheet("dm-nav-bar")
  }

  @Test
  fun `collapsed horizontal bar keeps the labels for assistive technology and adds titles`() {
    render(DmNavBar("nav", items, Orientation.HORIZONTAL, collapsed = true))
    assertThat(tag("nav").getAttribute("class")).contains("dm-nav-bar-horizontal", "dm-nav-bar-collapsed")
    assertThat(tags("link")[0].getAttribute("title")).isEqualTo("Home")
    assertThat(tag("label", 0).value).isEqualTo("Home")
  }
}
