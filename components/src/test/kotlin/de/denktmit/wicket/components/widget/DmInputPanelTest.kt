package de.denktmit.wicket.components.widget

import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.IMarkupResourceStreamProvider
import org.apache.wicket.MarkupContainer
import org.apache.wicket.model.Model
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.StringResourceStream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmInputPanelTest : WidgetTestBase() {

  /** An input of two words joined by a space, read from two native text inputs. */
  private class DmTwoWords(id: String, model: Model<String>) : DmInputPanel<String>(id, model), IMarkupResourceStreamProvider {
    val first = TextField("first", Model.of(""))
    val second = TextField("second", Model.of(""))

    override fun addComponents() {
      +first
      +second
    }

    override fun labelTarget(): FormComponent<*> = first

    override fun onBeforeRender() {
      val words = modelObject?.split(" ") ?: emptyList()
      first.modelObject = words.getOrNull(0) ?: ""
      second.modelObject = words.getOrNull(1) ?: ""
      super.onBeforeRender()
    }

    override fun convertInput() {
      convertedInput = listOf(first.convertedInput, second.convertedInput).filterNot { it.isNullOrBlank() }.joinToString(" ")
    }

    override fun getMarkupResourceStream(container: MarkupContainer, containerClass: Class<*>): IResourceStream =
      StringResourceStream("<wicket:panel><input wicket:id=\"first\" type=\"text\"/> <input wicket:id=\"second\" type=\"text\"/></wicket:panel>")
  }

  @Test
  fun `the form reads the composite value from the native inputs without script`() {
    val model = Model.of("hello world")
    val form = Form<Unit>("form")
    val input = DmTwoWords("words", model)
    form.add(input)
    render(form, "<form wicket:id=\"form\"><div wicket:id=\"words\"></div></form>")
    assertHooks(input)
    assertThat(tag("first").getAttribute("value")).isEqualTo("hello")
    assertThat(input.labelTarget()).isSameAs(input.first)

    tester.newFormTester("form").apply {
      setValue("words:first", "good")
      setValue("words:second", "night")
      submit()
    }
    assertThat(model.`object`).isEqualTo("good night")
  }
}
