package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmListItem
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

  @Test
  fun `populateItem delegates to lambda`() {
    var captured: String? = null
    val view = DmListView<String, List<String>>("list", Model.ofList(listOf("a"))) {
      captured = it
    }
    val item = DmListItem(0, Model.of("a"))

    invokeDeclared(view, "populateItem", item)

    assertThat(captured).isEqualTo("a")
  }

  @Test
  fun `non-inline onComponentTag keeps original tag name`() {
    val view = DmListView("list", Model.ofList(listOf("a")), inline = false) {}
    val tag = ComponentTag("ul", XmlTag.TagType.OPEN)

    invokeDeclared(view, "onComponentTag", tag)

    assertThat(tag.name).isEqualTo("ul")
  }

  @Test
  fun `DmListItem destructuring returns model object`() {
    val item = DmListItem(0, Model.of("a"))

    assertThat(item.component1()).isEqualTo("a")
  }

  @Test
  fun `DmListItem unaryPlus adds child component`() {
    val item = DmListItem(0, Model.of("a"))
    item.add(org.apache.wicket.markup.html.basic.Label("child", "x"))

    assertThat(item.get("child")).isNotNull
  }

  @Test
  fun `newItem returns DmListItem`() {
    val view = DmListView<String, List<String>>("list", Model.ofList(listOf("a"))) {}
    val item = invokeDeclared(view, "newItem", 0, Model.of("a"))

    assertThat(item).isInstanceOf(DmListItem::class.java)
  }
}
