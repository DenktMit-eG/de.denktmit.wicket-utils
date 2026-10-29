package de.denktmit.wicket.components.page

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.overlay.DmLoadingIndicator
import de.denktmit.wicket.components.overlay.DmNotifications
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.AttributeModifier
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.JavaScriptHeaderItem
import org.apache.wicket.markup.html.TransparentWebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.FeedbackPanel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.ResourceReference
import org.apache.wicket.util.string.Strings

/**
 * The page every application page extends (requirement K.103): `<html lang>` from the session locale, the title
 * from [title], the base sheet with tokens and primitives, the catalogue's own sheets as the widgets contribute them,
 * the feedback panel ([feedback]), the page-wide loading indicator ([loadingIndicator]), the notifications region
 * ([notifications]) and `<wicket:child/>` for the content. An application adds its own sheet through [styleSheets] and its navigation in its own base page.
 */
abstract class DmBasePage
@JvmOverloads
constructor(
  parameters: PageParameters? = null,
) : DmPage(parameters) {

  /** The document title. */
  open fun title(): IModel<String> = Model.of(javaClass.simpleName)

  /** Further style sheets the application links after the base sheet. */
  open fun styleSheets(): List<ResourceReference> = emptyList()

  /** The page's feedback panel; every message of the page lands here unless a form places its own. */
  lateinit var feedback: FeedbackPanel
    private set

  /** The page-wide loading indicator; Ajax calls with no indicator of their own may name it. */
  lateinit var loadingIndicator: DmLoadingIndicator
    private set

  /** The notifications region at the top right; `notifications.notify(text, tone, target)` after an action. */
  lateinit var notifications: DmNotifications
    private set

  override fun onInitialize() {
    super.onInitialize()
    +TransparentWebMarkupContainer("html").apply { add(AttributeModifier.replace("lang", Model.of(languageTag()))) }
    +Label("title", title())
    feedback = DmFeedbackPanel("feedback")
    +feedback
    loadingIndicator = DmLoadingIndicator("loading")
    loadingIndicator.addCssClass("dm-loading-indicator-page")
    +loadingIndicator
    notifications = DmNotifications("notifications")
    +notifications
  }

  override fun renderHead(response: IHeaderResponse) {
    super.renderHead(response)
    response.render(CssHeaderItem.forReference(DmResources.BASE_CSS))
    styleSheets().forEach { response.render(CssHeaderItem.forReference(it)) }
    response.render(JavaScriptHeaderItem.forReference(DmResources.CORE_JS))
  }

  /** The `lang` attribute value: the session locale's language tag. */
  protected fun languageTag(): String = session.locale?.toLanguageTag()?.takeIf { !Strings.isEmpty(it) && it != "und" } ?: "en"
}
