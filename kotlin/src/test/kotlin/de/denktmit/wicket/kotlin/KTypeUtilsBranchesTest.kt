package de.denktmit.wicket.kotlin

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KType

class KTypeUtilsBranchesTest {

  private class GenericHolder<T>

  @Test
  fun `toClass resolves type parameter upper bound`() {
    val typeParameter = GenericHolder::class.typeParameters.first().upperBounds.first()

    val result = typeParameter.toClass<Any>()

    assertThat(result).isEqualTo(Any::class.java)
  }

  @Test
  fun `toClass uses Any for type parameters without upper bounds`() {
    val parameter = mockk<KTypeParameter>()
    every { parameter.upperBounds } returns emptyList()
    val type = mockk<KType>()
    every { type.classifier } returns parameter

    val result = type.toClass<Any>()

    assertThat(result).isEqualTo(Any::class.java)
  }

  @Test
  fun `toClass throws when fallback javaType cannot be resolved`() {
    val type = mockk<KType>()
    every { type.classifier } returns null
    every { type.toString() } returns "unsupported-type"

    assertThatThrownBy { type.toClass<Any>() }
      .isInstanceOfAny(IllegalArgumentException::class.java, UnsupportedOperationException::class.java)
  }

  @Test
  fun `toClass resolves nullable type via KClass`() {
    val result = kotlin.reflect.typeOf<String?>().toClass<String>()

    assertThat(result).isEqualTo(String::class.java)
  }

  @Test
  fun `toClass resolves generic type parameter with explicit upper bound`() {
    class BoundedHolder<T : Comparable<T>>

    val typeParam = BoundedHolder::class.typeParameters.first().upperBounds.first()
    val result = typeParam.toClass<Comparable<*>>()

    assertThat(result).isEqualTo(Comparable::class.java)
  }
}
