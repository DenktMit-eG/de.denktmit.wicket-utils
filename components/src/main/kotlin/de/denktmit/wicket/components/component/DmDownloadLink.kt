package de.denktmit.wicket.components.component

import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.model.IModel
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler
import org.apache.wicket.util.lang.Bytes
import org.apache.wicket.util.resource.IResourceStream
import java.io.InputStream
import java.net.URLConnection
import java.time.Instant
import java.util.Locale

class DmDownloadLink(
  id: String,
  model: IModel<String>? = null,
  private val fileName: String,
  private val outputData: ByteArray,
  private val mimeType: String? = null,
) : Link<String>(id, model) {
  override fun onClick() {
    val contentType =
      mimeType
        ?: URLConnection.guessContentTypeFromName(fileName)
        ?: URLConnection.guessContentTypeFromStream(outputData.inputStream())
        ?: ""

    val resourceStream = ByteArrayResourceStream(outputData, contentType)

    requestCycle.scheduleRequestHandlerAfterCurrent(
      ResourceStreamRequestHandler(
        resourceStream,
      ).setFileName(fileName),
    )
  }
}

class ByteArrayResourceStream(
  private val outputData: ByteArray,
  private val contentType: String,
) : IResourceStream {
  private val inputStream = outputData.inputStream()

  override fun lastModifiedTime(): Instant = Instant.now()

  override fun close() {
    inputStream.close()
  }

  override fun getContentType(): String = contentType

  override fun length(): Bytes = Bytes.bytes(outputData.size.toLong())

  override fun getInputStream(): InputStream = inputStream

  private var locale: Locale = Locale.getDefault()

  override fun getLocale() = locale

  override fun setLocale(locale: Locale?) {
    if (locale != null) {
      this.locale = locale
    }
  }

  private var style = ""

  override fun getStyle(): String = style

  override fun setStyle(style: String?) {
    if (style != null) {
      this.style = style
    }
  }

  private var variation = ""

  override fun getVariation(): String = variation

  override fun setVariation(variation: String?) {
    if (variation != null) {
      this.variation = variation
    }
  }
}
