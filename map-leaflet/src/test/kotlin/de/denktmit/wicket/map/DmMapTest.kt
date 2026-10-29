package de.denktmit.wicket.map

import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmMapTest : WidgetTestBase() {

  private fun model() = MapModel(LatLng(52.5, 13.4), 10, listOf(Marker("hq", LatLng(52.52, 13.405), "Head office")), listOf(Shape("zone", ShapeKind.CIRCLE, listOf(LatLng(52.5, 13.4)), 500.0)))

  @Test
  fun `renders the model as data for the script, the markers as a list, and ships leaflet from the webjar`() {
    val map = render(DmMap("map", Model.of(model()), "20rem", drawable = true))
    assertHooks(map)
    assertThat(tag("map").getAttribute("data-dm-map-model")).contains("\"zoom\":10").contains("\"key\":\"hq\"").contains("\"kind\":\"circle\"").contains("\"drawable\":true")
    assertThat(tag("map").getAttribute("data-dm-map-call")).contains("Wicket.Ajax")
    assertThat(tag("map").getAttribute("style")).contains("--map-height:20rem")
    assertThat(tag("title").value).isEqualTo("Head office")
    assertThat(html).containsPattern("leaflet/1\\.9\\.4/leaflet(-ver-[0-9]+)?\\.js").containsPattern("leaflet/1\\.9\\.4/leaflet(-ver-[0-9]+)?\\.css")
    assertThat(html).containsPattern("dm-map-leaflet(-ver-[0-9]+)?\\.js")
    tester.startResourceReference(DmMap.LEAFLET_JS)
    assertThat(tester.lastResponse.status).isEqualTo(200)
    assertThat(tester.lastResponseAsString).contains("Leaflet")
  }

  @Test
  fun `events reach the page and the viewport lands in the model`() {
    val model = Model.of(model())
    var clicked: LatLng? = null
    var marker: String? = null
    var drawn: List<LatLng>? = null
    val map = render(DmMap("map", model).apply { onClick = { p, _ -> clicked = p }; onMarker = { k, _ -> marker = k }; onDraw = { p, _ -> drawn = p } })
    val behavior = map.behaviors.filterIsInstance<AbstractDefaultAjaxBehavior>().single()
    tester.request.setParameter("event", "viewport"); tester.request.setParameter("lat", "48.1"); tester.request.setParameter("lng", "11.5"); tester.request.setParameter("zoom", "12")
    tester.executeBehavior(behavior)
    assertThat(model.`object`.center).isEqualTo(LatLng(48.1, 11.5))
    assertThat(model.`object`.zoom).isEqualTo(12)
    tester.request.setParameter("event", "click"); tester.request.setParameter("lat", "1.5"); tester.request.setParameter("lng", "2.5")
    tester.executeBehavior(behavior)
    assertThat(clicked).isEqualTo(LatLng(1.5, 2.5))
    tester.request.setParameter("event", "marker"); tester.request.setParameter("key", "hq")
    tester.executeBehavior(behavior)
    assertThat(marker).isEqualTo("hq")
    tester.request.setParameter("event", "draw"); tester.request.setParameter("points", "1,2;3,4")
    tester.executeBehavior(behavior)
    assertThat(drawn).containsExactly(LatLng(1.0, 2.0), LatLng(3.0, 4.0))
  }
}
