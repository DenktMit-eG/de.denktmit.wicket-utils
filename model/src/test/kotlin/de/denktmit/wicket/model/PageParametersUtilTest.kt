package de.denktmit.wicket.model

import org.apache.wicket.request.mapper.parameter.PageParameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PageParametersUtilTest {

  private class TestParams : PageParams {
    override val pp = PageParameters()
    var name: String? by pp
  }

  @Test
  fun `property delegate reads and writes page parameters`() {
    val params = TestParams()

    params.name = "denktmit"

    assertThat(params.pp.get("name").toString()).isEqualTo("denktmit")
    assertThat(params.name).isEqualTo("denktmit")
  }

  @Test
  fun `set and add use kotlin property names as keys`() {
    val params = PageParameters()
    val source = object {
      val language: String? = "kotlin"
    }

    params.set(source::language, "java")
    params.add(source::language)

    assertThat(params.get("language").toString()).isEqualTo("java")
    assertThat(params.getAllNamed().count()).isEqualTo(2)
  }
}
