package de.denktmit.wicket.model

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ModelUtilsMapTest {

  @Test
  fun `map transforms model value`() {
    val source = Model.of(10)
    val mapped = source.map { it * 3 }

    assertThat(mapped.`object`).isEqualTo(30)
  }

  @Test
  fun `map to string`() {
    val source = Model.of(42)
    val mapped = source.map { "value=$it" }

    assertThat(mapped.`object`).isEqualTo("value=42")
  }

  @Test
  fun `map with null-safe model`() {
    val source: Model<String?> = Model.of("hello")
    val mapped = source.map { it?.uppercase() }

    assertThat(mapped.`object`).isEqualTo("HELLO")
  }

  @Test
  fun `chained maps compose`() {
    val source = Model.of(5)
    val mapped = source.map { it * 2 }.map { "result=$it" }

    assertThat(mapped.`object`).isEqualTo("result=10")
  }
}
