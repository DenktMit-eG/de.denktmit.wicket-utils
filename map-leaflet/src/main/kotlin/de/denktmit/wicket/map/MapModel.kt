package de.denktmit.wicket.map

import java.io.Serializable

/** A point on the map. */
data class LatLng(val lat: Double, val lng: Double) : Serializable

/** A marker: a key the page knows it by, a position and a title. */
data class Marker(val key: String, val position: LatLng, val title: String = "") : Serializable

/** The kind of a [Shape]. */
enum class ShapeKind { POLYLINE, POLYGON, CIRCLE }

/** A shape: a line or polygon over its points, or a circle around its first point with a radius in metres. */
data class Shape
@JvmOverloads
constructor(val key: String, val kind: ShapeKind, val points: List<LatLng>, val radiusMetres: Double = 0.0, val color: String = "#0b5f8a") : Serializable

/** A tile layer: the URL template, the attribution and the name shown in the layer switch. */
data class TileLayer
@JvmOverloads
constructor(val url: String, val attribution: String, val name: String = "Map") : Serializable

/**
 * The page's model of a map (requirement J.97): the viewport, markers, shapes and layers. The page never speaks
 * the library's language; a binding ([DmMap] over Leaflet) renders it and reports events back into it.
 */
data class MapModel
@JvmOverloads
constructor(
  var center: LatLng = LatLng(51.0, 10.0),
  var zoom: Int = 6,
  var markers: List<Marker> = emptyList(),
  var shapes: List<Shape> = emptyList(),
  var layers: List<TileLayer> = listOf(TileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", "© OpenStreetMap contributors", "OpenStreetMap")),
) : Serializable
