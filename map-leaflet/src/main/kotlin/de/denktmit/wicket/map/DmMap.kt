package de.denktmit.wicket.map

import com.github.openjson.JSONArray
import com.github.openjson.JSONObject
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.resource.WebjarResourceReference
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.CallbackParameter
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.request.cycle.RequestCycle
import org.apache.wicket.request.resource.PackageResourceReference
import org.apache.wicket.request.resource.ResourceReference

/**
 * The Leaflet binding of [MapModel] (requirement J.97): markers, shapes and layers from the model, the viewport
 * reported back into it after every move ([onViewport]), a click ([onClick]) and a drawn line ([onDraw], every
 * click adds a point while [drawable]). Leaflet comes from its webjar through the toolbox's resource reference.
 * Without script the map is a list of the markers with their positions.
 */
open class DmMap
@JvmOverloads
constructor(
  id: String,
  model: IModel<MapModel>,
  var height: String = "24rem",
  var drawable: Boolean = false,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, model, init) {

  /** Called after the user moved or zoomed; the model already holds the new viewport. */
  @Transient
  var onViewport: ((LatLng, Int, AjaxRequestTarget) -> Unit)? = null

  /** Called with the clicked position. */
  @Transient
  var onClick: ((LatLng, AjaxRequestTarget) -> Unit)? = null

  /** Called with the points drawn so far. */
  @Transient
  var onDraw: ((List<LatLng>, AjaxRequestTarget) -> Unit)? = null

  /** Called with the key of the clicked marker. */
  @Transient
  var onMarker: ((String, AjaxRequestTarget) -> Unit)? = null

  @Suppress("UNCHECKED_CAST")
  val map: MapModel get() = (defaultModel as IModel<MapModel>).`object`

  private fun param(name: String) = RequestCycle.get().request.requestParameters.getParameterValue(name)

  private val events = object : AbstractDefaultAjaxBehavior() {
    override fun respond(target: AjaxRequestTarget) {
      when (param("event").toString("")) {
        "viewport" -> {
          map.center = LatLng(param("lat").toDouble(), param("lng").toDouble())
          map.zoom = param("zoom").toInt()
          onViewport?.invoke(map.center, map.zoom, target)
        }
        "click" -> onClick?.invoke(LatLng(param("lat").toDouble(), param("lng").toDouble()), target)
        "marker" -> onMarker?.invoke(param("key").toString(""), target)
        "draw" -> onDraw?.invoke(param("points").toString("").split(";").filter { it.isNotBlank() }.map { val (a, b) = it.split(","); LatLng(a.toDouble(), b.toDouble()) }, target)
      }
    }
  }

  /** The model as the script reads it. */
  fun json(): String {
    val m = map
    return JSONObject()
      .put("center", JSONArray(listOf(m.center.lat, m.center.lng)))
      .put("zoom", m.zoom)
      .put("markers", JSONArray(m.markers.map { JSONObject().put("key", it.key).put("lat", it.position.lat).put("lng", it.position.lng).put("title", it.title) }))
      .put("shapes", JSONArray(m.shapes.map { JSONObject().put("key", it.key).put("kind", it.kind.name.lowercase()).put("points", JSONArray(it.points.map { p -> JSONArray(listOf(p.lat, p.lng)) })).put("radius", it.radiusMetres).put("color", it.color) }))
      .put("layers", JSONArray(m.layers.map { JSONObject().put("url", it.url).put("attribution", it.attribution).put("name", it.name) }))
      .put("drawable", drawable)
      .toString()
  }

  override fun addComponents() {
    outputMarkupId = true
    add(events)
    +object : ListView<Marker>("markers", object : IModel<List<Marker>> { override fun getObject() = map.markers }) {
      override fun populateItem(item: ListItem<Marker>) {
        item.add(Label("title", item.modelObject.title.ifEmpty { item.modelObject.key }))
        item.add(Label("position", "${item.modelObject.position.lat}, ${item.modelObject.position.lng}"))
      }
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-map", "")
    tag.put("data-dm-map-model", json())
    tag.put("data-dm-map-call", events.getCallbackFunction(CallbackParameter.explicit("event"), CallbackParameter.explicit("lat"), CallbackParameter.explicit("lng"), CallbackParameter.explicit("zoom"), CallbackParameter.explicit("key"), CallbackParameter.explicit("points")))
    tag.append("style", "--map-height:$height", ";")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(LEAFLET_CSS))
    response.render(JavaScriptHeaderItem.forReference(LEAFLET_JS))
  }

  override fun styleSheet(): ResourceReference = PackageResourceReference(DmMap::class.java, "dm-map.css")

  override fun script(): ResourceReference = PackageResourceReference(DmMap::class.java, "dm-map-leaflet.js")

  companion object {
    /** The Leaflet version the binding ships. */
    const val LEAFLET_VERSION = "1.9.4"

    @JvmField
    val LEAFLET_JS: ResourceReference = WebjarResourceReference("leaflet/$LEAFLET_VERSION/leaflet.js")

    @JvmField
    val LEAFLET_CSS: ResourceReference = WebjarResourceReference("leaflet/$LEAFLET_VERSION/leaflet.css")

    init {
      DmResources.BASE_CSS.hashCode()
    }
  }
}
