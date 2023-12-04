package de.denktmit.wicket.components.base

import de.denktmit.wicket.model.set
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import kotlin.reflect.KProperty

fun <P, T : String?> BookmarkablePageLink<P>.param(property: KProperty<T>, value: T) {
  pageParameters.set(property, value)
}
