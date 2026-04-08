package de.denktmit.wicket.components

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label

class TestRenderBorderPage : WebPage() {
  override fun onInitialize() {
    super.onInitialize()
    val border = TestRenderBorder("border")
    border.add(Label("inner", "inner-content"))
    add(border)
  }
}
