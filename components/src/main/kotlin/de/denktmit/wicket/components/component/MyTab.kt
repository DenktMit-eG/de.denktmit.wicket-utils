package de.denktmit.wicket.components.component

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel

class MyTab(
  model: IModel<String>,
  val markupContainer: WebMarkupContainer,
  val cssMobileIcon: String,
  @Transient
  val tooltip: (() -> String)? = null,
  val badge: IModel<String>? = null,
) : AbstractTab(model) {
  lateinit var tabCmp: WebMarkupContainer

  override fun getPanel(panelId: String?): WebMarkupContainer = markupContainer
}
