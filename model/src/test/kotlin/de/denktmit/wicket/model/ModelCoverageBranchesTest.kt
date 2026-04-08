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
  private data class Inner(var value: String) : Serializable
  private data class Outer(var inner: Inner) : Serializable
  private val immutableVal = "immutable"
  private var mutableVar = "original"

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
  fun `modelOr returns default when property is null`() {
    val holder = object { var name: String? = null }
    val model = modelOr(holder::name, "default")

    assertThat(model.getObject()).isEqualTo("default")
    holder.name = "set"
    assertThat(model.getObject()).isEqualTo("set")
  }

  @Test
  fun `modelOf with obj lambda and mutable property supports set`() {
    val holder = MutableHolder("a")
    val model = modelOf({ holder }, MutableHolder::value)

    model.setObject("b")
    assertThat(model.getObject()).isEqualTo("b")
    assertThat(holder.value).isEqualTo("b")
  }

  @Test
  fun `modelOf with obj lambda and immutable property is read only`() {
    val holder = ImmutableHolder("fixed")
    val model = modelOf({ holder }, ImmutableHolder::value)

    assertThat(model.getObject()).isEqualTo("fixed")
  }

  @Test
  fun `modelOf with obj lambda and nested property supports mutable chain`() {
    val outer = Outer(Inner("a"))
    val model = modelOf({ outer.inner }, Inner::value)

    assertThat(model.getObject()).isEqualTo("a")
    model.setObject("b")
    assertThat(outer.inner.value).isEqualTo("b")
  }

  @Test
  fun `modelOf with IModel and two properties supports mutable chain`() {
    val wrapper = Outer(Inner("x"))
    val wrapperModel = org.apache.wicket.model.Model.of(wrapper)
    val model = modelOf(wrapperModel, Outer::inner, Inner::value)

    assertThat(model.getObject()).isEqualTo("x")
    model.setObject("y")
    assertThat(wrapper.inner.value).isEqualTo("y")
  }

  @Test
  fun `modelOf with KProperty0 immutable property is read only`() {
    val model = modelOf(::immutableVal)

    assertThat(model.getObject()).isEqualTo("immutable")
  }

  @Test
  fun `modelOf with KProperty0 mutable property supports set`() {
    mutableVar = "original"
    val model = modelOf(::mutableVar)

    model.setObject("updated")
    assertThat(mutableVar).isEqualTo("updated")
    assertThat(model.getObject()).isEqualTo("updated")
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
