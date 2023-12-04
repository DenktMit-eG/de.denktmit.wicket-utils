package de.denktmit.wicket.components.resource

import org.apache.commons.fileupload2.core.FileItem
import org.apache.wicket.protocol.http.servlet.ServletWebRequest
import org.apache.wicket.request.resource.AbstractResource
import org.apache.wicket.request.resource.IResource
import org.apache.wicket.util.lang.Bytes

class FileUploadResource(
  private val filesKey: String,
  val handleFiles: (List<FileItem<*>>?) -> Unit
) : AbstractResource() {

  override fun newResourceResponse(attributes: IResource.Attributes?): ResourceResponse {
    val resourceResponse = ResourceResponse()
    val webRequest = attributes?.request as? ServletWebRequest
    try {
      val multiPartRequest = webRequest?.newMultipartWebRequest(Bytes.kilobytes(1100L), "ignored")
      multiPartRequest?.parseFileParts()
      val files = multiPartRequest?.files ?: throw IllegalArgumentException("")
      val fileItems: List<FileItem<*>>? = files[filesKey]
      handleFiles(fileItems)
    } catch (e: Exception) {
      throw java.lang.IllegalArgumentException("The Request was no MultiPart Request")
    }
    resourceResponse.writeCallback = object : WriteCallback() {
      override fun writeData(attributes: IResource.Attributes?) {
        attributes?.response?.write("Successfully uploaded".toByteArray())
      }
    }
    return resourceResponse
  }
}
