package de.denktmit.wicket.model

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.util.tester.WicketTester
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import kotlin.jvm.functions.Function1
import java.io.Serializable

class ModelCoverageBranchesTest {

  private class HomePage : WebPage()
  private data class MutableHolder(var value: String)
  private data class ImmutableHolder(val value: String) : Serializable

  @Suppress("UNCHECKED_CAST")
  @Test
  fun `map static function guards reified access`() {
    assertThatThrownBy {
      Class.forName("de.denktmit.wicket.model.ModelUtilsKt")
        .getDeclaredMethod("map", IModel::class.java, Function1::class.java)
        .invoke(
          null,
          Model.of(21),
          object : Function1<Int, Int> {
            override fun invoke(value: Int): Int = value * 2
          },
        )
    }.hasRootCauseInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `modelOf overload with setter and immutable projection are covered`() {
    val holder = MutableHolder("a")
    val withSetter = modelOf({ holder.value }, { holder.value = it })
    val immutableProjection = modelOf(Model.of(ImmutableHolder("x")), ImmutableHolder::value)

    withSetter.setObject("b")

    assertThat(withSetter.getObject()).isEqualTo("b")
    assertThat(holder.value).isEqualTo("b")
    assertThat(immutableProjection.getObject()).isEqualTo("x")
  }

  @Test
  fun `map model executes generated mapper lambda class`() {
    val mapped = Model.of(5).map { it * 3 }

    assertThat(mapped.getObject()).isEqualTo(15)
  }

  @Test
  fun `setResponsePage default block and lmodel members are covered`() {
    val tester =
      WicketTester(
        object : WebApplication() {
          override fun getHomePage() = HomePage::class.java
        },
      )

    try {
      val params = object : PageParams {
        override val pp = org.apache.wicket.request.mapper.parameter.PageParameters()
      }
      val label = Label("id", "x")
      label.setResponsePage(HomePage::class.java, params)

      val model = LModel(get = { "value" }, valueType = String::class.java)
      model.detach()

      assertThat(model.getObject()).isEqualTo("value")
      assertThat(model.get != null).isTrue()
      assertThat(model.set != null).isTrue()
      assertThat(model.valueType).isEqualTo(String::class.java)
    } finally {
      tester.destroy()
    }
  }
}
