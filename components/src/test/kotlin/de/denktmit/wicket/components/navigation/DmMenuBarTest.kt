package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmMenuBarTest : WidgetTestBase() {

  private var saved = 0

  private fun items() = listOf(
    MenuItem.menu("File", "folder", listOf(
      MenuItem.action("Save", "file", "Ctrl+S") { saved++ },
      MenuItem.separator(),
      MenuItem.page("Home", TestHomePage::class.java),
      MenuItem.action("Print", enabled = false) {},
    )),
    MenuItem.menu("View", children = listOf(
      MenuItem.check("Toolbar", checked = true),
      MenuItem.radio("Small", "size", checked = true),
      MenuItem.radio("Large", "size"),
    )),
  )

  @Test
  fun `renders a menubar with menus, separators, check and radio states, shortcuts and disabled items`() {
    val bar = render(DmMenuBar("menu", items()))
    assertHooks(bar)
    assertThat(tag("menu").getAttribute("data-dm-menu")).isEqualTo("bar")
    assertThat(html).contains("role=\"menubar\"").contains("role=\"menu\"")
    assertThat(html).containsPattern("<li[^>]*role=\"separator\"")
    assertThat(tags("link").map { it.getAttribute("role") }).contains("menuitem", "menuitemcheckbox", "menuitemradio")
    assertThat(tags("link").first { it.getAttribute("data-dm-shortcut") != null }.getAttribute("data-dm-shortcut")).isEqualTo("Ctrl+S")
    assertThat(tags("link").filter { it.getAttribute("aria-disabled") == "true" }).hasSize(1)
    assertThat(tags("link").filter { it.getAttribute("aria-checked") == "true" }).hasSize(2)
    assertThat(tags("link").filter { it.getAttribute("aria-haspopup") == "menu" }).hasSize(2)
    assertThat(tags("shortcut")).hasSize(1)
    assertStyleSheet("dm-menu")
    assertScript("dm-menu")
  }

  @Test
  fun `an action runs through its link, a radio switches its group, a check toggles`() {
    val items = items()
    render(DmMenuBar("menu", items))
    tester.clickLink("menu:menu:items:0:submenu:items:0:link")
    assertThat(saved).isEqualTo(1)

    tester.clickLink("menu:menu:items:1:submenu:items:2:link")
    assertThat(items[1].children[1].checked).isFalse()
    assertThat(items[1].children[2].checked).isTrue()

    tester.clickLink("menu:menu:items:1:submenu:items:0:link")
    assertThat(items[1].children[0].checked).isFalse()
    assertThat(tags("link").filter { it.getAttribute("aria-checked") == "true" }.size).isEqualTo(1)
  }

  @Test
  fun `a page item is a bookmarkable link`() {
    render(DmMenuBar("menu", items()))
    tester.clickLink("menu:menu:items:0:submenu:items:2:link")
    tester.assertRenderedPage(TestHomePage::class.java)
  }

  @Test
  fun `the popup menu is a details element that can open from another widget`() {
    val owner = org.apache.wicket.markup.html.basic.Label("owner", "right-click me")
    val popup = DmPopupMenu("popup", items(), org.apache.wicket.model.Model.of("Actions")).openFrom(owner)
    val box = org.apache.wicket.markup.html.WebMarkupContainer("box").apply { add(owner, popup) }
    render(box, "<div wicket:id=\"box\"><span wicket:id=\"owner\"></span><div wicket:id=\"popup\"></div></div>")
    assertHooks(popup)
    assertThat(tag("popup").getAttribute("data-dm-menu")).isEqualTo("popup")
    assertThat(tag("popup").getAttribute("data-dm-menu-for")).isEqualTo(tag("owner").getAttribute("id"))
    assertThat(html).contains("<details class=\"dm-popup-menu\"").contains("Actions")
    assertThat(tag("menu").getAttribute("role")).isEqualTo("menu")
  }
}
