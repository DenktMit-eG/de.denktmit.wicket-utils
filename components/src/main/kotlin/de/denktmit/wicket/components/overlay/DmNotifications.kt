package de.denktmit.wicket.components.overlay

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.display.Tone
import de.denktmit.wicket.components.layout.Side
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmWidgetPanel
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.request.resource.ResourceReference
import java.io.Serializable

/** One notification: its text, tone and how long it stays, in milliseconds (`0` until dismissed). */
data class Notification
@JvmOverloads
constructor(val text: String, val tone: Tone = Tone.INFO, val duration: Int = 5000) : Serializable

/**
 * Messages that appear at an edge for a time and go away (requirement H.78), triggered by the server after an
 * action: [notify] adds one and refreshes the region through the Ajax target; script removes it after its
 * duration or on its close button. Without script the messages show until the next full render, after which
 * they are gone. The base page places one region; a page may place its own.
 */
open class DmNotifications
@JvmOverloads
constructor(
  id: String,
  var side: Side = Side.TOP,
  init: DmWidgetPanel.() -> Unit = {},
) : DmWidgetPanel(id, null, init) {

  private val pending = ArrayList<Notification>()

  /** The notifications shown on the next render. */
  val notifications: List<Notification> get() = pending

  /** Shows [notification]; with a [target] the region refreshes in place. */
  @JvmOverloads
  fun notify(notification: Notification, target: AjaxRequestTarget? = null) {
    pending.add(notification)
    target?.add(this)
  }

  /** Shows [text] in [tone]; with a [target] the region refreshes in place. */
  @JvmOverloads
  fun notify(text: String, tone: Tone = Tone.INFO, target: AjaxRequestTarget? = null) = notify(Notification(text, tone), target)

  override fun addComponents() {
    outputMarkupId = true
    +object : ListView<Notification>("items", object : org.apache.wicket.model.IModel<List<Notification>> { override fun getObject() = pending.toList() }) {
      override fun populateItem(item: ListItem<Notification>) {
        val notification = item.modelObject
        item.add(Label("text", notification.text))
        item.add(DmIcon("icon", when (notification.tone) { Tone.SUCCESS -> "success"; Tone.WARNING -> "warning"; Tone.DANGER -> "error"; else -> "info" }))
        item.add(DmIcon("closeIcon", "close"))
        item.add(object : org.apache.wicket.behavior.Behavior() {
          override fun onComponentTag(component: org.apache.wicket.Component, tag: ComponentTag) {
            tag.append("class", "dm-tone-${notification.tone.name.lowercase()}", " ")
            tag.put("data-dm-notification-duration", notification.duration.toString())
            tag.put("role", if (notification.tone == Tone.DANGER || notification.tone == Tone.WARNING) "alert" else "status")
          }
        })
      }
    }
  }

  override fun onAfterRender() {
    super.onAfterRender()
    pending.clear()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-notifications", "")
    tag.put("aria-live", "polite")
    tag.append("class", "dm-notifications-${side.name.lowercase()}", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-notification")

  override fun script(): ResourceReference = DmResources.js("dm-notification")
}
