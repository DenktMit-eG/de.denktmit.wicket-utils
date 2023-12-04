package de.denktmit.wicket.components.resource

import org.apache.wicket.Application
import org.apache.wicket.request.http.WebResponse
import org.apache.wicket.request.resource.IResource

class ClassPathResource(
    val path: String
) : IResource {

    override fun respond(attributes: IResource.Attributes) {
        val resources = Thread.currentThread().contextClassLoader.getResources(path)
        val resource = resources.asSequence().firstOrNull()
        val response = attributes.response as WebResponse

        resource?.let {
            val mimeType = Application.get().getMimeType(path)
            response.setContentType(mimeType)
            it.openStream().buffered().copyTo(response.outputStream)
        }
            ?: throw RuntimeException("Could not send resource '$path' -> '$resource'")
    }

}