package de.denktmit.wicket.components.input

import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmInputGroupTest : InputTestBase() {

  @Test
  fun `prefix, suffix and button around the author's input`() {
    val group = DmInputGroup("amount", "€", "per hour").button(object : Link<Void>(DmInputGroup.BUTTON) { override fun onClick() {} }.apply { setBody(Model.of("Go")) })
    group.add(TextField("rate", Model.of("120")))
    renderInForm(group, "<span wicket:id=\"amount\"><input wicket:id=\"rate\" type=\"text\"/></span>")
    assertHooks(group)
    assertThat(tag("prefix").value).isEqualTo("€")
    assertThat(tag("suffix").value).isEqualTo("per hour")
    assertThat(html.indexOf("€")).isLessThan(html.indexOf("wicket:id=\"rate\""))
    assertThat(tag("button").value).isEqualTo("Go")
    assertStyleSheet("dm-input-group")
  }

  @Test
  fun `without prefix, suffix and button only the input renders`() {
    val group = DmInputGroup("amount")
    group.add(TextField("rate", Model.of("1")))
    renderInForm(group, "<span wicket:id=\"amount\"><input wicket:id=\"rate\" type=\"text\"/></span>")
    assertThat(tester.getTagByWicketId("prefix")).isNull()
    assertThat(tester.getTagByWicketId("button")).isNull()
  }
}
