package de.denktmit.wicket.kotlin

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.jvm.javaType

@Suppress("UNCHECKED_CAST")
fun <T> KType.toClass(): Class<T> =
    when (val cl = classifier) {
        is KClass<*> -> cl.javaObjectType
        is KTypeParameter -> cl.upperBounds.firstOrNull()?.toClass<T>() ?: Any::class.java
        else -> when (val jType = javaType) {
            is Class<*> -> jType
            is ParameterizedType -> jType.rawType
            else -> null
        }
    } as? Class<T> ?: throw IllegalArgumentException("No corresponding java class found for '$this'")
