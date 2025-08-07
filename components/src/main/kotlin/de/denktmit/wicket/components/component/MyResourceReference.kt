package de.denktmit.wicket.components.component

import org.apache.wicket.request.resource.ByteArrayResource
import org.apache.wicket.request.resource.IResource
import org.apache.wicket.request.resource.ResourceReference

class MyResourceReference(
  val key: String,
) : ResourceReference(key) {
  override fun getResource(): IResource =
    ByteArrayResource("image/png", ByteArray(0)).apply {
    }
}
