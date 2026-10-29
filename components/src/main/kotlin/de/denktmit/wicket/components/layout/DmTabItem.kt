package de.denktmit.wicket.components.layout

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model

/**
 * One tab of a [DmTabBox] or [DmAccordion] (requirement C.17): a title, an optional icon from the sprite, an
 * optional badge, whether the user may close it, and the factory of its panel, called with the id the panel must
 * use. The factory runs when the tab is first shown, so an expensive panel costs nothing until then.
 */
open class DmTabItem
@JvmOverloads
constructor(
  title: IModel<String>,
  /** A symbol name from the toolbox's sprite, `null` for none. */
  val icon: String? = null,
  /** A short mark next to the title (a count, a state), `null` for none. */
  val badge: IModel<String>? = null,
  /** Whether the tab shows a close control. */
  val closable: Boolean = false,
  @Transient
  private val panel: (String) -> WebMarkupContainer,
) : AbstractTab(title) {

  @JvmOverloads
  constructor(title: String, icon: String? = null, badge: String? = null, closable: Boolean = false, panel: (String) -> WebMarkupContainer) :
    this(Model.of(title), icon, badge?.let { Model.of(it) }, closable, panel)

  override fun getPanel(panelId: String): WebMarkupContainer = panel(panelId)
}
