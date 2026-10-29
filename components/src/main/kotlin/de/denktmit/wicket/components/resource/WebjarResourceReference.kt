package de.denktmit.wicket.components.resource

import org.apache.wicket.request.resource.IResource
import org.apache.wicket.request.resource.ResourceReference

/**
 * A script or sheet from a packaged web archive (webjar) on the classpath (requirement J, Q6): [path] is the part
 * after `META-INF/resources/webjars/`, version included, like `chart.js/4.6.1/dist/chart.umd.js`. The version in
 * the path makes the URL change with the library, so the browser caches it for as long as it likes.
 */
class WebjarResourceReference(val path: String) : ResourceReference(WebjarResourceReference::class.java, path) {
  override fun getResource(): IResource = ClassPathResource("META-INF/resources/webjars/$path")
}
