package de.denktmit.wicket.components.component

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.classNameAsCssClass
import de.denktmit.wicket.components.resource.ClassPathResource
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.image.Image
import org.apache.wicket.request.resource.ByteArrayResource
import org.apache.wicket.request.resource.IResource
import kotlin.reflect.KProperty0

open class MyImage(
  id: String,
  var resource: IResource,
  var antiCache: List<String> = listOf(),
) : Image(id, resource) {
  constructor(
    field: KProperty0<String>,
    id: String = field.name,
    imageResource: IResource = ClassPathResource(field.get()),
  ) : this(id, imageResource)

  override fun onConfigure() {
    super.onConfigure()
    visibility?.let { setVisible(it()) }
    imageResource = resource
  }

  fun reloadAntiCache(
    antiCacheProfile: List<String>,
    newBytes: ByteArray?,
  ) {
    antiCache = antiCacheProfile
    resource = ByteArrayResource("image/png", newBytes)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-image", "dm-id-$id", "dm-cmp-${classNameAsCssClass()}")
  }

  override fun shouldAddAntiCacheParameter(): Boolean = super.shouldAddAntiCacheParameter() || antiCache.isNotEmpty()

  @Transient
  var visibility: (() -> Boolean)? = null

  override fun addAntiCacheParameter(tag: ComponentTag?) {
    if (antiCache.isNotEmpty()) {
      var url = tag!!.attributes.getString("src")
      url += if (url.contains("?")) "&" else "?"
      url += "v=" + antiCache.joinToString("_").replace(" ", "-")
      tag.put("src", url)
    } else {
      super.addAntiCacheParameter(tag)
    }
  }

  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }
}
