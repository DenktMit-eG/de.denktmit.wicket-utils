package de.denktmit.wicket.model

import de.denktmit.wicket.kotlin.toClass
import org.apache.wicket.model.IModel
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.reflect.jvm.reflect

@OptIn(ExperimentalReflectionOnLambdas::class)
class LModel<T> internal constructor(
    val get: () -> T,
    val set: (T) -> Unit = {},
    override val valueType: Class<T> = get.reflect()?.returnType?.toClass()!!
) : IModel<T>, ValueType<T> {
    override fun detach() {}
    override fun getObject(): T = get()
    override fun setObject(obj: T) {
        set(obj)
    }
}


fun <T> modelOf(get: () -> T): IModel<T> =
    LModel(get)

fun <T> modelOf(get: () -> T, set: (T) -> Unit): IModel<T> =
    LModel(get, set)

fun <T> modelOf(property: KProperty0<T>): IModel<T> =
    if (property is KMutableProperty0<T>)
        LModel(
            property::get,
            property::set,
            valueType = property.returnType.toClass()
        )
    else
        LModel(
            property::get,
            valueType = property.returnType.toClass()
        )

fun <T : Any> modelOr(property: KProperty0<T?>, onNull: T): IModel<T> =
    LModel(
        { property.get() ?: onNull },
        valueType = property.returnType.toClass()
    )

fun <O, T> modelOf(obj: () -> O, property: KProperty1<O, T>) =
    if (property is KMutableProperty1<O, T>)
        LModel(
            { property.get(obj()) },
            { property.set(obj(), it) },
            property.returnType.toClass()
        )
    else
        LModel(
            { property.get(obj()) },
            valueType = property.returnType.toClass()
        )

fun <T, U> modelOf(property1: KProperty0<T>, property2: KProperty1<T, U>): IModel<U> =
    if (property2 is KMutableProperty1<T, U>)
        LModel(
            { property2.get(property1.get()) },
            { property2.set(property1.get(), it) },
            valueType = property2.returnType.toClass()
        )
    else
        LModel(
            { property2.get(property1.get()) },
            valueType = property2.returnType.toClass()
        )

fun <S, T> modelOf(original: IModel<S>, property: KProperty1<S, T>) =
    if (property is KMutableProperty1<S, T>)
        LModel(
            { property.get(original.`object`) },
            { property.set(original.`object`, it) },
            valueType = property.returnType.toClass()
        )
    else
        LModel(
            { property.get(original.`object`) },
            valueType = property.returnType.toClass()
        )

fun <S, T, U> modelOf(original: IModel<S>, property1: KProperty1<S, T>, property2: KProperty1<T, U>) =
    if (property2 is KMutableProperty1<T, U>)
        LModel(
            { property2.get(property1.get(original.`object`)) },
            { property2.set(property1.get(original.`object`), it) },
            valueType = property2.returnType.toClass()
        )
    else
        LModel(
            { property2.get(property1.get(original.`object`)) },
            valueType = property2.returnType.toClass()
        )
