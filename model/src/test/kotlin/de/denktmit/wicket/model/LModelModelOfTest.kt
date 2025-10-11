package de.denktmit.wicket.model

import org.apache.wicket.model.IModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LModelTest {

  @Test
  fun `modelOf(get) returns IModel that delegates to get`() {
    var calls = 0
    val get = {
      calls++
      "value-$calls"
    }

    val model: IModel<String> = modelOf(get)

    assertThat(model.getObject()).isEqualTo("value-1")
    assertThat(model.getObject()).isEqualTo("value-2")
  }
}
