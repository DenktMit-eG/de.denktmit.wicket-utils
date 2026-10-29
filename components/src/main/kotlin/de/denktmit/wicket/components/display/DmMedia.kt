package de.denktmit.wicket.components.display

import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/** A media file with the browser's controls (requirement G.73): the base of audio and video. */
abstract class DmMedia(
  id: String,
  src: IModel<String>,
  private val element: String,
  /** The accessible name of the player. */
  val label: IModel<String>,
) : WebMarkupContainer(id, src) {

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.name = element
    tag.put("src", defaultModelObjectAsString)
    tag.put("controls", "controls")
    tag.put("preload", "metadata")
    tag.put("aria-label", label.`object`)
  }
}

/** An audio file with the browser's controls (requirement G.73). */
open class DmAudio
@JvmOverloads
constructor(id: String, src: IModel<String>, label: IModel<String> = Model.of("Audio")) : DmMedia(id, src, "audio", label) {
  @JvmOverloads
  constructor(id: String, src: String, label: String = "Audio") : this(id, Model.of(src), Model.of(label))
}

/** A video file with the browser's controls (requirement G.73). */
open class DmVideo
@JvmOverloads
constructor(id: String, src: IModel<String>, label: IModel<String> = Model.of("Video")) : DmMedia(id, src, "video", label) {
  @JvmOverloads
  constructor(id: String, src: String, label: String = "Video") : this(id, Model.of(src), Model.of(label))
}
