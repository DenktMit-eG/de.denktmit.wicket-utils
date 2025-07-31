@file:OptIn(ExperimentalReflectionOnLambdas::class)

package de.loosetie.util.wicket

import de.loosetie.utils.toClass
import org.apache.wicket.Component
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas

class LModel<T>(
  @Transient
  val get: () -> T,
  @Transient
  val set: (T) -> Unit = {},
  override val valueType: Class<T>,
) : IModel<T>,
  ValueType<T> {
  override fun detach() {}

  override fun getObject(): T = get()

  override fun setObject(obj: T) {
    set(obj)
  }
}

interface ValueType<T> {
  val valueType: Class<out T>
}

// TODO See GenericBaseModel#createSerializableVersionOf
inline fun <reified T> modelOf(noinline get: () -> T): IModel<T> = LModel(get, valueType = T::class.java)

inline fun <reified T> modelOf(
  noinline get: () -> T,
  noinline set: (T) -> Unit,
): IModel<T> = LModel(get, set, valueType = T::class.java)

fun <T> modelOf(property: KProperty0<T>): IModel<T> =
  if (property is KMutableProperty0<T>) {
    LModel(
      property::get,
      property::set,
      valueType = property.returnType.toClass(),
    )
  } else {
    LModel(
      property::get,
      valueType = property.returnType.toClass(),
    )
  }

fun <T : Any> modelOr(
  property: KProperty0<T?>,
  onNull: T,
): IModel<T> =
  LModel(
    { property.get() ?: onNull },
    valueType = property.returnType.toClass(),
  )

fun <O, T> modelOf(
  obj: () -> O,
  property: KProperty1<O, T>,
) = if (property is KMutableProperty1<O, T>) {
  LModel(
    { property.get(obj()) },
    { property.set(obj(), it) },
    property.returnType.toClass(),
  )
} else {
  LModel(
    { property.get(obj()) },
    valueType = property.returnType.toClass(),
  )
}

fun <T, U> modelOf(
  property1: KProperty0<T>,
  property2: KProperty1<T, U>,
): IModel<U> =
  if (property2 is KMutableProperty1<T, U>) {
    LModel(
      { property2.get(property1.get()) },
      { property2.set(property1.get(), it) },
      valueType = property2.returnType.toClass(),
    )
  } else {
    LModel(
      { property2.get(property1.get()) },
      valueType = property2.returnType.toClass(),
    )
  }

fun <S, T> modelOf(
  original: IModel<S>,
  property: KProperty1<S, T>,
) = if (property is KMutableProperty1<S, T>) {
  LModel(
    { property.get(original.`object`) },
    { property.set(original.`object`, it) },
    valueType = property.returnType.toClass(),
  )
} else {
  LModel(
    { property.get(original.`object`) },
    valueType = property.returnType.toClass(),
  )
}

fun <S, T, U> modelOf(
  original: IModel<S>,
  property1: KProperty1<S, T>,
  property2: KProperty1<T, U>,
) = if (property2 is KMutableProperty1<T, U>) {
  LModel(
    { property2.get(property1.get(original.`object`)) },
    { property2.set(property1.get(original.`object`), it) },
    valueType = property2.returnType.toClass(),
  )
} else {
  LModel(
    { property2.get(property1.get(original.`object`)) },
    valueType = property2.returnType.toClass(),
  )
}

fun <T> nullableModelOf(get: () -> T?): IModel<T?> =
  object : IModel<T?> {
    override fun detach() {}

    override fun getObject(): T? = get()

    override fun setObject(obj: T?) {}
  }

fun <T> nullableModelOf(
  get: () -> T?,
  set: (T?) -> Unit,
): IModel<T?> =
  object : IModel<T?> {
    override fun detach() {}

    override fun getObject(): T? = get()

    override fun setObject(obj: T?) {
      set(obj)
    }
  }

inline fun <T, reified R> IModel<T>.map(crossinline mapper: (T) -> R): IModel<R> = modelOf { `object`.let(mapper) }

fun <T> Component.fieldModel() = invokeHiddenMethod(Component::class.java, this, "initModel") as? IModel<T>

private fun <T> invokeHiddenMethod(
  clazz: Class<T>,
  obj: T,
  name: String,
  vararg args: Any?,
) = clazz.getDeclaredMethod(name).let {
  it.isAccessible = true
  it.invoke(obj, *args)
}

fun <V, T> IModel<V>.transform(get: (V) -> T): IModel<T> =
  LModel(
    { get(`object`) },
    valueType = Any::class.java as Class<T>,
  )

fun <V, T> IModel<V>.transform(
  get: (V) -> T,
  set: (T) -> V,
): IModel<T> =
  LModel(
    { get(`object`) },
    { `object` = set(it) },
    valueType = Any::class.java as Class<T>,
  )
