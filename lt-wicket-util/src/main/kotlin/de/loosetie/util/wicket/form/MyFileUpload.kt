package de.loosetie.util.wicket.form

import de.loosetie.util.wicket.component.addCssClass
import org.apache.wicket.Component
import org.apache.wicket.markup.html.form.upload.FileUploadField

class MyFileUpload(
  id: String,
  @Transient
  val init: () -> Unit = {},
) : FileUploadField(id) {
  operator fun Component.unaryPlus() {
    this@MyFileUpload.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-file-upload", "lt-id-$id")
    init()
  }
}
