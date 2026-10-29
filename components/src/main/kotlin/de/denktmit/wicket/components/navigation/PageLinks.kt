package de.denktmit.wicket.components.navigation

import org.apache.wicket.Page
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.request.mapper.parameter.PageParameters

/** A bookmarkable link to [page]; captures the page class so callers with `Class<out Page>` need no cast. */
internal fun <C : Page> pageLink(id: String, page: Class<C>, parameters: PageParameters): BookmarkablePageLink<Void> =
  BookmarkablePageLink<Void, C>(id, page, parameters)
