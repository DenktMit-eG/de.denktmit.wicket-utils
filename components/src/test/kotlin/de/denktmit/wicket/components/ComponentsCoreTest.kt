package de.denktmit.wicket.components

import de.denktmit.wicket.components.base.DmComponent
import de.denktmit.wicket.components.base.DmContainer
import de.denktmit.wicket.components.base.param
import de.denktmit.wicket.components.behavior.CssClassRemover
import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.behavior.ReadonlyBehavior
import de.denktmit.wicket.components.behavior.disabled
import de.denktmit.wicket.components.choice.DmChoiceRenderer
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.components.visitor.ComponentVisitor
import de.denktmit.wicket.components.visitor.PathVisitor
import io.mockk.mockk
import io.mockk.verify
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.model.Model
import org.apache.wicket.util.visit.IVisit
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ComponentsCoreTest : WicketTestBase() {

  private class TestPage : WebPage()

  private fun invokeProtected(target: Any, name: String, vararg args: Any?): Any? {
    val types = args.map { it?.javaClass ?: String::class.java }.toTypedArray()
    val method = target::class.java.declaredMethods.first { it.name == name && it.parameterCount == args.size }
    method.isAccessible = true
    return method.invoke(target, *args)
  }

  @Test
  fun `utility classes and css conversion work`() {
    val option = SelectOption("k", "v")

    assertThat(option.key).isEqualTo("k")
    assertThat(option.value).isEqualTo("v")
    assertThat(option.classNameAsCssClass()).isEqualTo("-select-option")
  }

  @Test
  fun `wicket request util resolves host`() {
    assertThat(WicketRequestUtil.getHost()).isNotBlank()
  }

  @Test
  fun `base components can be toggled and ajax enabled`() {
    val component = DmComponent("c")
    val container = DmContainer("k")
    component.visible = { false }
    container.visible = { false }

    component.outputMarkupId = true
    component.outputMarkupPlaceholderTag = true
    container.outputMarkupId = true
    container.outputMarkupPlaceholderTag = true

    invokeProtected(component, "onConfigure")
    invokeProtected(container, "onConfigure")

    assertThat(component.isVisible).isFalse()
    assertThat(container.isVisible).isFalse()
  }

  @Test
  fun `bookmarkable link param writes page parameter`() {
    val link = BookmarkablePageLink<Any, TestPage>("link", TestPage::class.java)
    val sample = object { val q: String? = "value" }

    link.param(sample::q, "abc")

    assertThat(link.pageParameters.get("q").toString()).isEqualTo("abc")
  }

  @Test
  fun `behaviors return expected attribute values`() {
    val cssClassRemover = CssClassRemover("my-class")
    val disabledBehavior = DisabledBehavior { false }
    val readonlyBehavior = ReadonlyBehavior { true }

    val removed = invokeProtected(cssClassRemover, "newValue", "my-class x", "my-class")
    val disabled = invokeProtected(disabledBehavior, "newValue", null, "disabled")
    val readonly = invokeProtected(readonlyBehavior, "newValue", null, "true")

    assertThat(removed as String).contains("x")
    assertThat(disabled).isEqualTo(org.apache.wicket.AttributeModifier.VALUELESS_ATTRIBUTE_REMOVE)
    assertThat(readonly).isEqualTo("true")

    val label = Label("label", "text")
    label.disabled { true }
    assertThat(label.behaviors).hasSize(2)
  }

  @Test
  fun `choice renderer resolves ids and display values`() {
    val renderer = DmChoiceRenderer<String>(displayValue = { it.uppercase() }, idValue = { value, _ -> value })
    val choices = Model.ofList(listOf("a", "b"))

    assertThat(renderer.getDisplayValue("a")).isEqualTo("A")
    assertThat(renderer.getObject("b", choices)).isEqualTo("b")
    assertThat(renderer.getObject("", choices)).isNull()
    assertThatThrownBy { renderer.getObject("x", choices) }
      .isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test
  fun `list view creates dm list item`() {
    val view = DmListView("rows", Model.ofList(listOf("a"))) {}

    val item = invokeProtected(view, "newItem", 0, Model.of("a"))

    assertThat(item?.javaClass?.simpleName).isEqualTo("DmListItem")
  }

  @Test
  fun `visitors stop traversal for matching ids`() {
    val label = Label("target", "txt")
    val componentVisit = mockk<IVisit<org.apache.wicket.Component>>(relaxed = true)
    val pathVisit = mockk<IVisit<String>>(relaxed = true)

    ComponentVisitor("target").component(label, componentVisit)
    PathVisitor<Label, String>("target").component(label, pathVisit)

    verify(exactly = 1) { componentVisit.stop(label) }
    verify(exactly = 1) { pathVisit.stop(any()) }
  }
}
