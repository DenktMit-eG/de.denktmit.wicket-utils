package de.denktmit.wicket.model

import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.Model
import org.apache.wicket.model.IModel
import org.apache.wicket.Page
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.util.tester.WicketTester
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ModelUtilsAndMetadataTest {

  @MountPage(path = "/sample", deployment = false)
  private class AnnotatedPage

  private class TypedValue : ValueType<String> {
    override val valueType: Class<String> = String::class.java
  }

  private class TestHomePage : WebPage()

  private class ModelBackedLabel(id: String) : Label(id) {
    override fun initModel(): IModel<*> = Model.of("wicket")
  }

  @Test
  fun `map projects model values`() {
    val mapped = Model.of(21).map { it * 2 }

    assertThat(mapped.`object`).isEqualTo(42)
  }

  @Test
  fun `fieldModel returns component model via hidden initModel`() {
    val tester =
      WicketTester(
        object : WebApplication() {
          override fun getHomePage(): Class<out Page> = TestHomePage::class.java
        },
      )
    val fieldModel =
      try {
        val label = ModelBackedLabel("name")
        label.fieldModel<String>()
      } finally {
        tester.destroy()
      }

    assertThat(fieldModel.`object`).isEqualTo("wicket")
  }

  @Test
  fun `mount page annotation keeps configured metadata`() {
    val annotation = AnnotatedPage::class.java.getAnnotation(MountPage::class.java)

    assertThat(annotation.path).isEqualTo("/sample")
    assertThat(annotation.deployment).isFalse()
  }

  @Test
  fun `value type exposes raw class`() {
    assertThat(TypedValue().valueType).isEqualTo(String::class.java)
  }
}
