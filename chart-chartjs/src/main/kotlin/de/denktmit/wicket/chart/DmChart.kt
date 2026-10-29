package de.denktmit.wicket.chart

import com.github.openjson.JSONArray
import com.github.openjson.JSONObject
import de.denktmit.wicket.components.resource.WebjarResourceReference
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.attributes.CallbackParameter
import org.apache.wicket.markup.ComponentTag
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
 * The Chart.js binding of [ChartModel] (requirement J.98): bar, line, area, pie, scatter and gauge (a half
 * doughnut) with legend, axes and tooltips as the model says, a click on a point reported to [onSelect], and an
 * export of the chart as a PNG through the export link the script fills. Chart.js comes from its webjar. Without
 * script the chart is a table of its data.
 */
open class DmChart
@JvmOverloads
constructor(
  id: String,
  model: IModel<ChartModel>,
  var height: String = "20rem",
  /** The accessible name of the chart and the title of the data table. */
  val title: IModel<String> = org.apache.wicket.model.Model.of("Chart"),
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, model, init) {

  /** Called with the series and point index the user clicked. */
  @Transient
  var onSelect: ((Int, Int, AjaxRequestTarget) -> Unit)? = null

  @Suppress("UNCHECKED_CAST")
  val chart: ChartModel get() = (defaultModel as IModel<ChartModel>).`object`

  private val select = object : AbstractDefaultAjaxBehavior() {
    override fun respond(target: AjaxRequestTarget) {
      val p = RequestCycle.get().request.requestParameters
      onSelect?.invoke(p.getParameterValue("series").toInt(), p.getParameterValue("point").toInt(), target)
    }
  }

  /** The model as the script reads it. */
  fun json(): String {
    val c = chart
    return JSONObject()
      .put("type", c.type.name.lowercase())
      .put("labels", JSONArray(c.labels))
      .put("series", JSONArray(c.series.map { s ->
        JSONObject().put("name", s.name).put("values", JSONArray(s.values)).put("color", s.color ?: JSONObject.NULL)
          .put("points", JSONArray(s.points.map { JSONObject().put("x", it.first).put("y", it.second) }))
      }))
      .put("xTitle", c.xTitle).put("yTitle", c.yTitle).put("legend", c.legend).put("tooltips", c.tooltips).put("max", c.max)
      .toString()
  }

  override fun addComponents() {
    outputMarkupId = true
    add(select)
    +Label("caption", title)
    +object : ListView<String>("heads", object : IModel<List<String>> { override fun getObject() = chart.labels }) {
      override fun populateItem(item: ListItem<String>) {
        item.add(Label("head", item.modelObject))
      }
    }
    +object : ListView<Series>("rows", object : IModel<List<Series>> { override fun getObject() = chart.series }) {
      override fun populateItem(item: ListItem<Series>) {
        item.add(Label("name", item.modelObject.name))
        item.add(object : ListView<Double>("cells", item.modelObject.values) {
          override fun populateItem(cell: ListItem<Double>) {
            cell.add(Label("cell", cell.modelObject.toString()))
          }
        })
      }
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-chart", "")
    tag.put("data-dm-chart-model", json())
    tag.put("data-dm-chart-title", title.`object`)
    tag.put("data-dm-chart-call", select.getCallbackFunction(CallbackParameter.explicit("series"), CallbackParameter.explicit("point")))
    tag.append("style", "--chart-height:$height", ";")
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(JavaScriptHeaderItem.forReference(CHARTJS))
  }

  override fun styleSheet(): ResourceReference = PackageResourceReference(DmChart::class.java, "dm-chart.css")

  override fun script(): ResourceReference = PackageResourceReference(DmChart::class.java, "dm-chart-chartjs.js")

  companion object {
    /** The Chart.js version the binding ships. */
    const val CHARTJS_VERSION = "4.6.1"

    @JvmField
    val CHARTJS: ResourceReference = WebjarResourceReference("chart.js/$CHARTJS_VERSION/dist/chart.umd.js")
  }
}
