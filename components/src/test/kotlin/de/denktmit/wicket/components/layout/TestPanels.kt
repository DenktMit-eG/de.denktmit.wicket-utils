package de.denktmit.wicket.components.layout

import org.apache.wicket.MarkupContainer
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream

/** A panel with one line of text, for slots that take a component. */
class TextPanel(id: String, text: String) : Panel(id), IMarkupResourceStreamProvider {
  init {
    add(Label("text", text))
  }

  override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream =
    StringResourceStream("<wicket:panel><p wicket:id=\"text\" class=\"text-panel\"></p></wicket:panel>")
}
