package de.denktmit.wicket.components.navigation

import org.apache.wicket.Page
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler
import org.apache.wicket.core.request.handler.PageProvider
import org.apache.wicket.request.IRequestHandler
import org.apache.wicket.request.mapper.parameter.PageParameters

/** The request handler whose URL leads to [page]; for components that need a page URL without a link component. */
fun pageLinkFor(page: Class<out Page>, parameters: PageParameters): IRequestHandler = BookmarkablePageRequestHandler(PageProvider(page, parameters))
