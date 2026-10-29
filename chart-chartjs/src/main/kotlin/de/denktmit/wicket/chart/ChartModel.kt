package de.denktmit.wicket.chart

import java.io.Serializable

/** The kinds of chart the model describes. */
enum class ChartType { BAR, LINE, AREA, PIE, SCATTER, GAUGE }

/** One series: a name, its values (one per label, or x/y pairs for a scatter) and an optional colour. */
data class Series
@JvmOverloads
constructor(val name: String, val values: List<Double>, val color: String? = null, val points: List<Pair<Double, Double>> = emptyList()) : Serializable

/**
 * The page's model of a chart (requirement J.98): the type, the labels, the series, axis titles, legend and
 * tooltips. A binding ([DmChart] over Chart.js) renders it and reports the selection back to the page.
 */
data class ChartModel
@JvmOverloads
constructor(
  var type: ChartType = ChartType.BAR,
  var labels: List<String> = emptyList(),
  var series: List<Series> = emptyList(),
  var xTitle: String = "",
  var yTitle: String = "",
  var legend: Boolean = true,
  var tooltips: Boolean = true,
  /** The full scale of a gauge. */
  var max: Double = 100.0,
) : Serializable
