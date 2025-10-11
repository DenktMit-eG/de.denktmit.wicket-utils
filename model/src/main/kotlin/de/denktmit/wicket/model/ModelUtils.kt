package de.denktmit.wicket.model

import org.apache.wicket.Component
import org.apache.wicket.model.IModel


inline fun <T, reified R> IModel<T>.map(crossinline mapper: (T) -> R): IModel<R> =
  modelOf { `object`.let(mapper) }


fun <T> Component.fieldModel() =
  invokeHiddenMethod(Component::class.java, this, "initModel") as? IModel<T>
    ?: throw RuntimeException("${this.javaClass}.initModel() did not return a valid model")

private fun <T> invokeHiddenMethod(clazz: Class<T>, obj: T, name: String, vararg args: Any?) =
  clazz.getDeclaredMethod(name).let {
    it.isAccessible = true
    it.invoke(obj, *args)
  }
