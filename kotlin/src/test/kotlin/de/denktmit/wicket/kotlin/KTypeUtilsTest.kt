package de.denktmit.wicket.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.typeOf

class KTypeUtilsTest {

  @Test
  fun `toClass resolves plain kotlin types`() {
    val result = typeOf<String>().toClass<String>()

    assertThat(result).isEqualTo(String::class.java)
  }

  @Test
  fun `toClass resolves parameterized raw types`() {
    val result = typeOf<List<Int>>().toClass<List<Int>>()

    assertThat(result).isEqualTo(List::class.java)
  }
}
