package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmListView
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.parser.XmlTag
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmListViewTest : WicketTestBase() {

  @Test
  fun `inline mode switches tag name`() {
    val view = DmListView("list", Model.ofList(listOf("a")), inline = true) {}
    val tag = ComponentTag("ul", XmlTag.TagType.OPEN)
    view.enableAjax()

    invokeDeclared(view, "onComponentTag", tag)

    assertThat(tag.name).isEqualTo("span")
    assertThat(view.outputMarkupId).isTrue()
    assertThat(view.outputMarkupPlaceholderTag).isTrue()
  }
}
