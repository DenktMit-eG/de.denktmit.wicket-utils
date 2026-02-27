package de.denktmit.wicket.model

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.util.tester.WicketTester
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LModelOverloadsTest {

  private data class Address(var street: String)
  private data class Person(var name: String, var address: Address)

  private class Params : PageParams {
    override val pp = PageParameters()
    var q: String? by pp
  }

  private class HomePage : WebPage()
  private class NullableHolder(var value: String?)
  private class StringHolder(var value: String)
  private class PersonHolder(var person: Person)

  @Test
  fun `modelOf with mutable property zero reads and writes`() {
    val holder = StringHolder("a")

    val model = modelOf(holder::value)
    model.setObject("b")

    assertThat(model.getObject()).isEqualTo("b")
    assertThat(holder.value).isEqualTo("b")
  }

  @Test
  fun `modelOr returns fallback when original is null`() {
    val holder = NullableHolder(null)

    val model = modelOr(holder::value, "fallback")

    assertThat(model.getObject()).isEqualTo("fallback")
  }

  @Test
  fun `modelOf object supplier and nested properties supports updates`() {
    val person = Person("A", Address("Main"))
    val rootModel = modelOf({ person }, Person::name)
    val nestedFromProp = modelOf(modelOf { person }, Person::address, Address::street)

    rootModel.setObject("B")
    nestedFromProp.setObject("Second")

    assertThat(person.name).isEqualTo("B")
    assertThat(person.address.street).isEqualTo("Second")
  }

  @Test
  fun `modelOf composed properties supports updates`() {
    val holder = PersonHolder(Person("A", Address("Main")))

    val nested = modelOf(holder::person, Person::address)
    val street = modelOf(nested, Address::street)

    street.setObject("Third")

    assertThat(holder.person.address.street).isEqualTo("Third")
  }

  @Test
  fun `setResponsePage extension applies params block`() {
    val tester =
      WicketTester(
        object : WebApplication() {
          override fun getHomePage(): Class<out org.apache.wicket.Page> = HomePage::class.java
        },
      )

    try {
      val params = Params()
      val label = Label("id", "x")

      label.setResponsePage(HomePage::class.java, params) {
        q = "query"
      }

      assertThat(params.pp.get("q").toString()).isEqualTo("query")
    } finally {
      tester.destroy()
    }
  }
}
