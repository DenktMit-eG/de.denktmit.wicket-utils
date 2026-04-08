package de.denktmit.wicket.components

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.Model

class TestRenderListBorderPage : WebPage() {
  override fun onInitialize() {
    super.onInitialize()
    add(TestRenderListBorder("listBorder", Model.ofList(listOf("a", "b"))))
  }
}
