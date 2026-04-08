package de.denktmit.wicket.components

import de.denktmit.wicket.components.border.Border
import org.apache.wicket.markup.html.basic.Label

class TestRenderBorder(id: String) : Border(id) {
  override fun addComponents() {
    addToBorder(bodyContainer)
    addToBorder(Label("label", "border-label"))
  }
}
