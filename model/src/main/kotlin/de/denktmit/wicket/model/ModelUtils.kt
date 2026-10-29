package de.denktmit.wicket.model

import org.apache.wicket.Component
import org.apache.wicket.model.IModel


inline fun <T, reified R> IModel<T>.map(crossinline mapper: (T) -> R): IModel<R> =
  modelOf { `object`.let(mapper) }


/** The model a component initialises for itself (`initModel`), typed as the caller expects it; the type is the caller's promise. */
@Suppress("UNCHECKED_CAST")
fun <T> Component.fieldModel() =
  invokeHiddenMethod(Component::class.java, this, "initModel") as? IModel<T>
    ?: throw RuntimeException("${this.javaClass}.initModel() did not return a valid model")

private fun <T> invokeHiddenMethod(clazz: Class<T>, obj: T, name: String, vararg args: Any?) =
  clazz.getDeclaredMethod(name).let {
    it.isAccessible = true
    it.invoke(obj, *args)
  }
