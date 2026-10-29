package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.upload.FileUpload
import org.apache.wicket.markup.html.form.upload.FileUploadField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import org.apache.wicket.util.lang.Bytes
import org.apache.wicket.validation.ValidationError

/**
 * One or many files by dialog or by drop (requirement E.51): a native file input inside a drop zone, the accepted
 * types and the maximum size as the browser's constraints and as server-side checks, the chosen files listed by
 * script before the upload and the uploaded ones ([uploaded]) by the server after it, and Wicket's own progress
 * bar when [progress] is on (the application enables upload progress updates). Without script the file dialog
 * and the submit do the work.
 */
open class DmFileDrop
@JvmOverloads
constructor(
  id: String,
  model: IModel<List<FileUpload>>,
  val multiple: Boolean = true,
  /** The `accept` attribute, `null` for any type. */
  val accept: String? = null,
  /** The largest file accepted, `null` for no limit. */
  val maxSize: Bytes? = null,
  val progress: Boolean = false,
  /** The names of the files uploaded so far, shown below the zone. */
  val uploaded: IModel<out List<String>> = Model.ofList(emptyList()),
  val prompt: IModel<String> = Model.of("Drop files here or choose"),
  init: DmInputPanel<List<FileUpload>>.() -> Unit = {},
) : DmInputPanel<List<FileUpload>>(id, model, init) {

  /** The native file input; it keeps its own model so that a rejected submit never touches the page's. */
  val input: FileUploadField = object : FileUploadField("files", org.apache.wicket.model.util.ListModel(ArrayList())) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      if (multiple) tag.put("multiple", "multiple")
      accept?.let { tag.put("accept", it) }
    }
  }

  override fun addComponents() {
    outputMarkupId = true
    +input
    +Label("prompt", prompt)
    +DmIcon("icon", "upload")
    +object : ListView<String>("uploaded", uploaded) {
      override fun populateItem(item: ListItem<String>) {
        item.add(Label("name", item.modelObject))
      }
    }
    +(if (progress) UploadProgressBar("progress", findParent(org.apache.wicket.markup.html.form.Form::class.java), input) else WebMarkupContainer("progress").apply { isVisible = false })
  }

  override fun labelTarget(): FormComponent<*> = input

  override fun convertInput() {
    val files = input.fileUploads ?: emptyList()
    val limit = maxSize
    if (limit != null) {
      files.filter { it.size > limit.bytes() }.forEach {
        error(ValidationError().addKey("tooLarge").setVariable("file", it.clientFileName).setVariable("max", limit.toString()).setMessage("${it.clientFileName} is larger than $limit."))
      }
    }
    convertedInput = files
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("data-dm-file-drop", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-file-drop")

  override fun script(): ResourceReference = DmResources.js("dm-file-drop")
}
