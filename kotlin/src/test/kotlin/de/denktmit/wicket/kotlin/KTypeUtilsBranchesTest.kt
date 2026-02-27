package de.denktmit.wicket.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class KTypeUtilsBranchesTest {

  private class GenericHolder<T>

  @Test
  fun `toClass resolves type parameter upper bound`() {
    val typeParameter = GenericHolder::class.typeParameters.first().upperBounds.first()

    val result = typeParameter.toClass<Any>()

    assertThat(result).isEqualTo(Any::class.java)
  }

  @Test
  fun `toClass resolves from java class type`() {
    val rawType: KType = String::class.createType()

    val result = rawType.toClass<String>()

    assertThat(result).isEqualTo(String::class.java)
  }
}
