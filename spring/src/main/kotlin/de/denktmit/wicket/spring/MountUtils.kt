package de.denktmit.wicket.spring

import de.denktmit.wicket.model.MountPage
import org.apache.wicket.Page
import org.apache.wicket.core.request.mapper.MountedMapper
import org.apache.wicket.protocol.http.WebApplication

fun WebApplication.mountAnnotatedPages() {
  findClasses<Page>(javaClass.packageName, annotations = listOf(MountPage::class.java))
    .mapNotNull { it.getAnnotation(MountPage::class.java)?.let { a -> it to a } }
    .forEach { (pageClass, mountPage) ->
      mount(MountedMapper(mountPage.path, pageClass))
    }
}
