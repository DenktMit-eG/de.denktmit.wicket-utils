package de.denktmit.wicket.components.layout

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.resource.DmResources
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.request.resource.ResourceReference

/**
 * A child centred in its container (requirement C.24): horizontally through the `center` primitive, and vertically
 * too when [vertical] is set, which stretches the container to [minHeight].
 */
open class DmCenter
@JvmOverloads
constructor(
  id: String,
  /** Centre vertically as well, within [minHeight]. */
  var vertical: Boolean = false,
  /** The height the container takes when centring vertically, a CSS length. */
  var minHeight: String = "100%",
  /** The widest the centred content may be, a CSS length. */
  var measure: String = "var(--measure)",
  @Transient
  private val setup: DmCenter.() -> Unit = {},
) : DmLayoutContainer(id) {

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("center", "center-intrinsic")
    setup()
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-center")

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    if (vertical) tag.append("class", "dm-center-vertical", " ")
    tag.append("style", "--measure-wide:$measure;--min-height:$minHeight", ";")
  }
}

/** A child stretched to fill its container (requirement C.24). */
open class DmFit
@JvmOverloads
constructor(
  id: String,
  @Transient
  private val setup: DmFit.() -> Unit = {},
) : DmLayoutContainer(id) {
  override fun onInitialize() {
    super.onInitialize()
    setup()
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-center")
}
