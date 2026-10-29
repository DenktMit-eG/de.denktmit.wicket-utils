package de.denktmit.wicket.components.input

import org.apache.wicket.markup.html.form.upload.FileUpload
import org.apache.wicket.model.util.ListModel
import org.apache.wicket.util.file.File
import org.apache.wicket.util.lang.Bytes
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.file.Files

class DmFileDropTest : InputTestBase() {

  @Test
  fun `a native file input in a drop zone with accept, multiple and the uploaded list`() {
    val model = ListModel<FileUpload>(emptyList())
    val drop = renderInForm(DmFileDrop("photos", model, multiple = true, accept = "image/*", uploaded = ListModel(listOf("old.png"))))
    assertHooks(drop)
    assertThat(tag("files").getAttribute("type")).isEqualTo("file")
    assertThat(tag("files").getAttribute("multiple")).isNotNull
    assertThat(tag("files").getAttribute("accept")).isEqualTo("image/*")
    assertThat(tag("name").value).isEqualTo("old.png")
    assertThat(tester.getTagByWicketId("progress")).isNull()
    assertStyleSheet("dm-file-drop")
    assertScript("dm-file-drop")
  }

  @Test
  fun `an uploaded file reaches the model, a file over the limit is rejected`() {
    val model = ListModel<FileUpload>(emptyList())
    val file = Files.createTempFile("drop", ".txt").toFile().apply { writeText("hello"); deleteOnExit() }
    renderInForm(DmFileDrop("doc", model, multiple = false, maxSize = Bytes.bytes(3)))
    form().apply { setFile("doc:files", File(file), "text/plain"); submit() }
    assertThat(model.`object`).isEmpty()
    tester.assertErrorMessages("${file.name} is larger than 3 bytes.")

    renderInForm(DmFileDrop("doc", model, multiple = false, maxSize = Bytes.kilobytes(1)))
    form().apply { setFile("doc:files", File(file), "text/plain"); submit() }
    assertThat(model.`object`).hasSize(1)
    assertThat(model.`object`[0].clientFileName).isEqualTo(file.name)
  }

  @Test
  fun `the progress bar is wicket's own when asked for`() {
    tester.application.applicationSettings.isUploadProgressUpdatesEnabled = true
    renderInForm(DmFileDrop("doc", ListModel(emptyList()), progress = true))
    tester.assertComponent("form:doc:progress", org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar::class.java)
  }
}
