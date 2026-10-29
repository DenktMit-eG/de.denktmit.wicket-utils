package de.denktmit.wicket.components.resource

import org.apache.wicket.Application
import org.apache.wicket.resource.loader.ClassStringResourceLoader

/**
 * The toolbox's message bundle (`DmMessages.properties`, English and German): the texts of form-level rules and
 * of every widget that speaks to the user. Widget-level texts are found next to the widget class on their own;
 * form-level rules need the bundle registered once per application with [install].
 */
object DmMessages {
  /** Registers the bundle with [application]; call it once from `WebApplication.init`. */
  @JvmStatic
  fun install(application: Application) {
    val loaders = application.resourceSettings.stringResourceLoaders
    if (loaders.none { it is ClassStringResourceLoader && it.toString().contains(DmMessages::class.java.name) }) {
      loaders.add(DmMessagesLoader())
    }
  }

  /** The loader that reads `DmMessages.properties` next to this object. */
  class DmMessagesLoader : ClassStringResourceLoader(DmMessages::class.java) {
    override fun toString(): String = "DmMessagesLoader(" + DmMessages::class.java.name + ")"
  }
}
