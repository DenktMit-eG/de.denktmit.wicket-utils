package de.denktmit.wicket.components

import de.denktmit.wicket.components.resource.FileUploadResource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.commons.fileupload2.core.FileItem
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest
import org.apache.wicket.protocol.http.servlet.ServletWebRequest
import org.apache.wicket.request.http.WebResponse
import org.apache.wicket.request.resource.IResource
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class FileUploadResourceTest : WicketTestBase() {

  @Test
  fun `constructs with files key`() {
    val resource = FileUploadResource("files") {}

    assertThat(resource).isNotNull
  }

  @Test
  fun `newResourceResponse processes multipart files and writes success response`() {
    val attrs = mockk<IResource.Attributes>()
    val request = mockk<ServletWebRequest>()
    val multipart = mockk<MultipartServletWebRequest>()
    val response = mockk<WebResponse>(relaxed = true)
    val output = ByteArrayOutputStream()
    val item = mockk<FileItem<*>>(relaxed = true)
    var handled: List<FileItem<*>>? = null
    val resource = FileUploadResource("files") { handled = it }
    every { attrs.request } returns request
    every { attrs.response } returns response
    every { response.outputStream } returns output
    every { request.newMultipartWebRequest(any(), any()) } returns multipart
    every { multipart.parseFileParts() } returns Unit
    every { multipart.files } returns mapOf("files" to listOf(item))

    val rr = invokeDeclared(resource, "newResourceResponse", attrs) as org.apache.wicket.request.resource.AbstractResource.ResourceResponse
    rr.writeCallback.writeData(attrs)

    assertThat(handled).containsExactly(item)
    verify { response.write(any<ByteArray>()) }
  }

  @Test
  fun `newResourceResponse throws for non multipart request`() {
    val attrs = mockk<IResource.Attributes>()
    every { attrs.request } returns mockk(relaxed = true)

    assertThatThrownBy { invokeDeclared(FileUploadResource("files") {}, "newResourceResponse", attrs) }
      .isInstanceOf(java.lang.reflect.InvocationTargetException::class.java)
      .hasCauseInstanceOf(IllegalArgumentException::class.java)
      .hasRootCauseMessage("The Request was no MultiPart Request")
  }
}
