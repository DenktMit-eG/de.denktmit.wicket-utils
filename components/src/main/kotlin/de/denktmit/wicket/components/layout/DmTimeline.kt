package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.Component
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.basic.MultiLineLabel
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One event on a [DmTimeline]: a title, the time as the page shows it (and as a machine-readable value), a body. */
data class TimelineEvent
@JvmOverloads
constructor(
  val title: String,
  val time: String,
  val body: String = "",
  /** The `datetime` attribute value, ISO 8601; `null` leaves it out. */
  val dateTime: String? = null,
) : Serializable

/**
 * Events in order along a line (requirement C.30), each with a title, a time and a body. The body is a multi-line
 * text by default; [body] replaces it with a component per event (id [BODY]).
 */
open class DmTimeline
@JvmOverloads
constructor(
  id: String,
  events: IModel<List<TimelineEvent>>,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, events, init) {

  @JvmOverloads
  constructor(id: String, events: List<TimelineEvent>, init: DmWidgetPanel.() -> Unit = {}) : this(id, Model.ofList(events), init)

  @Transient
  private var bodyFactory: ((TimelineEvent) -> Component)? = null

  /** Builds the body of every event; the component's id must be [BODY]. */
  fun body(factory: (TimelineEvent) -> Component): DmTimeline {
    bodyFactory = factory
    return this
  }

  override fun addComponents() {
    @Suppress("UNCHECKED_CAST")
    +object : ListView<TimelineEvent>("events", defaultModel as IModel<List<TimelineEvent>>) {
      override fun populateItem(item: ListItem<TimelineEvent>) {
        val event = item.modelObject
        item.add(Label("time", event.time).apply { event.dateTime?.let { add(org.apache.wicket.AttributeModifier.replace("datetime", it)) } })
        item.add(Label("title", event.title))
        item.add(bodyFactory?.invoke(event)?.also { require(it.id == BODY) { "body id must be '$BODY'" } } ?: MultiLineLabel(BODY, event.body))
      }
    }
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-timeline")

  companion object {
    /** The id a body component must carry. */
    const val BODY = "body"
  }
}
