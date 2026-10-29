package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmAnchorLinkTest : WidgetTestBase() {

  @Test
  fun `links to the target's markup id and marks the target`() {
    val region = Label("region", "Rates")
    val link = DmAnchorLink("jump", region, "Go to rates")
    val box = WebMarkupContainer("box").apply { add(link, region) }
    render(box, "<div wicket:id=\"box\"><a wicket:id=\"jump\"></a><section wicket:id=\"region\"></section></div>")
    assertHooks(link)
    assertThat(tag("jump").getAttribute("href")).isEqualTo("#" + tag("region").getAttribute("id"))
    assertThat(tag("jump").value).isEqualTo("Go to rates")
    assertThat(tag("region").getAttribute("class")).contains("dm-anchor-target")
    assertStyleSheet("dm-anchor-link")
  }
}
