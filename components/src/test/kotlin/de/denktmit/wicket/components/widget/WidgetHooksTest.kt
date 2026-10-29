package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.base.DmContainer
import org.apache.wicket.markup.html.WebMarkupContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WidgetHooksTest : WidgetTestBase() {

  private open class DmTabBox(id: String) : WebMarkupContainer(id)

  @Test
  fun `kind is the kebab-cased class name without the Dm prefix`() {
    assertThat(WidgetHooks.kindCssClass(DmTabBox("x"))).isEqualTo("dm-tab-box")
    assertThat(WidgetHooks.idCssClass(DmTabBox("x"))).isEqualTo("dm-id-x")
  }

  @Test
  fun `an anonymous subclass keeps the kind of its named parent`() {
    val anonymous = object : DmTabBox("x") {}
    assertThat(WidgetHooks.kindCssClass(anonymous)).isEqualTo("dm-tab-box")
  }

  @Test
  fun `a class without Dm prefix falls back to its own name`() {
    assertThat(WidgetHooks.kindCssClass(WebMarkupContainer("x"))).isEqualTo("dm-web-markup-container")
  }

  @Test
  fun `a marked container renders both hooks`() {
    val container = render(DmContainer("box"))
    assertHooks(container)
    assertThat(tag("box").getAttribute("class")).contains("dm-container")
  }
}
