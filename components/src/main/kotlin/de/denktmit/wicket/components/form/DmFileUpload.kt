package de.denktmit.wicket.components.form

import de.denktmit.wicket.components.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.markup.html.form.upload.FileUploadField

class DmFileUpload(
  id: String,
  @Transient
  val init: () -> Unit = {},
) : FileUploadField(id) {
  operator fun Component.unaryPlus() {
    this@DmFileUpload.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("dm-file-upload", "dm-id-$id")
    init()
  }
}
