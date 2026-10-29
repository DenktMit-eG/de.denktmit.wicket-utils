package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmEditorTest : InputTestBase() {

  @Test
  fun `the textarea carries the source and the submitted value is sanitised`() {
    val model = Model<String?>("<p>Hi</p>")
    val editor = renderInForm(DmEditor("about", model, rows = 4))
    assertHooks(editor)
    assertThat(tag("source").name).isEqualTo("textarea")
    assertThat(tag("source").getAttribute("rows")).isEqualTo("4")
    assertThat(tag("source").value).isEqualTo("&lt;p&gt;Hi&lt;/p&gt;")
    assertThat(tag("about").getAttribute("data-dm-editor")).isNotNull
    assertStyleSheet("dm-editor")
    assertScript("dm-editor")
    form().apply { setValue("about:source", "<p onclick=\"x\">Changed</p><script>bad()</script>"); submit() }
    assertThat(model.`object`).isEqualTo("<p>Changed</p>")

    form().apply { setValue("about:source", "<script>only</script>"); submit() }
    assertThat(model.`object`).isNull()
  }
}
