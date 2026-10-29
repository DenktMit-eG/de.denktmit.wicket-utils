package de.denktmit.wicket.components.resource

import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.request.resource.JavaScriptResourceReference
import org.apache.wicket.request.resource.PackageResourceReference
import org.apache.wicket.request.resource.ResourceReference

/**
 * The toolbox's shared resources and the lookup for a widget's own sheet or script (requirements A.7, K.105): every
 * reference is a package resource, versioned by the toolbox's artefact and cached by the browser; Wicket renders each
 * one once per page however many widgets contribute it.
 *
 * Files live under `de/denktmit/wicket/components/resource/`: `css/<name>.css` and `js/<name>.js`, one per widget.
 */
object DmResources {

  /** Tokens, reset and the Every Layout primitives; the sheet every page starts from. */
  @JvmField
  val BASE_CSS: ResourceReference = CssResourceReference(DmResources::class.java, "css/dm-base.css")

  /** The enhancement loop the widget scripts register with: runs on load and after every Ajax update. */
  @JvmField
  val CORE_JS: ResourceReference = JavaScriptResourceReference(DmResources::class.java, "js/dm-core.js")

  /** The inline SVG symbol sprite the icon widget references. */
  @JvmField
  val ICONS: ResourceReference = PackageResourceReference(DmResources::class.java, "dm-icons.svg")

  /** The style sheet of the widget [name] (`css/<name>.css`). */
  @JvmStatic
  fun css(name: String): ResourceReference = CssResourceReference(DmResources::class.java, "css/$name.css")

  /** The script of the widget [name] (`js/<name>.js`). */
  @JvmStatic
  fun js(name: String): ResourceReference = JavaScriptResourceReference(DmResources::class.java, "js/$name.js")
}
