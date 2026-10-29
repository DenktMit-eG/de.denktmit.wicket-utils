package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.model.util.ListModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPortalLayoutTest : WidgetTestBase() {

  private fun layout(): DmPortalLayout {
    val order = ListModel<List<String>>(listOf(listOf("jobs", "news"), listOf("team")))
    return DmPortalLayout("portal", order, { key, id -> TextPanel(id, "panel $key") })
  }

  @Test
  fun `renders the columns in order with move links and the reorder callback`() {
    val portal = render(layout())
    assertHooks(portal)
    assertThat(tags("columns")).hasSize(2)
    assertThat(tags("panels").map { it.getAttribute("data-dm-portal-panel") }).containsExactly("jobs", "news", "team")
    assertThat(tag("portal").getAttribute("data-dm-portal-callback")).contains("Wicket.Ajax")
    assertThat(tags("up")).hasSize(3)
    assertStyleSheet("dm-portal-layout")
    assertScript("dm-portal-layout")
  }

  @Test
  fun `move links reorder by a full request and report the order`() {
    var reported: List<List<String>>? = null
    val portal = render(layout().apply { onReorder = { reported = it } })
    tester.clickLink("portal:columns:0:panels:1:next")
    assertThat(portal.columns).containsExactly(listOf("jobs"), listOf("team", "news"))
    assertThat(reported).isEqualTo(portal.columns)
    tester.clickLink("portal:columns:1:panels:1:up")
    assertThat(portal.columns).containsExactly(listOf("jobs"), listOf("news", "team"))
    tester.clickLink("portal:columns:0:panels:0:down")
    assertThat(portal.columns).containsExactly(listOf("jobs"), listOf("news", "team"))
  }

  @Test
  fun `the ajax callback takes the order the script reports`() {
    val portal = render(layout())
    val behavior = portal.behaviors.filterIsInstance<org.apache.wicket.ajax.AbstractDefaultAjaxBehavior>().single()
    tester.request.setParameter("order", "team,news|jobs")
    tester.executeBehavior(behavior)
    assertThat(portal.columns).containsExactly(listOf("team", "news"), listOf("jobs"))
  }
}
