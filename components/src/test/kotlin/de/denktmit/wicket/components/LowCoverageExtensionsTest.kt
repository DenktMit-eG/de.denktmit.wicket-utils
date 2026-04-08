package de.denktmit.wicket.components

import de.denktmit.wicket.components.base.DmComponent
import de.denktmit.wicket.components.base.enableAjax
import de.denktmit.wicket.components.base.setResponsePage
import de.denktmit.wicket.components.behavior.DisabledBehavior
import de.denktmit.wicket.components.component.DmDropDownChoice
import de.denktmit.wicket.components.page.DmPage
import de.denktmit.wicket.model.PageParams
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LowCoverageExtensionsTest : WicketTestBase() {

  private class TargetPage : WebPage()
  private class Holder(var selected: String?)

  private class UnaryPlusPage : DmPage(PageParameters()) {
    init {
      +Label("content", "value")
      +DisabledBehavior { true }
    }
  }

  @Test
  fun `dm component top level helpers are covered`() {
    val component = DmComponent("cmp")
    Class.forName("de.denktmit.wicket.components.base.DmComponentKt")
      .getDeclaredMethod(
        "unaryPlus",
        org.apache.wicket.Component::class.java,
        org.apache.wicket.behavior.Behavior::class.java,
      )
      .invoke(null, component, DisabledBehavior { true })
    component.enableAjax()

    val params =
      object : PageParams {
        override val pp = PageParameters()
      }
    val label = Label("id", "value")
    label.setResponsePage(TargetPage::class.java, params)

    assertThat(component.outputMarkupId).isTrue()
    assertThat(component.outputMarkupPlaceholderTag).isTrue()
    assertThat(component.behaviors).isNotEmpty
  }

  @Test
  fun `css util remove class and queue helper are covered`() {
    val label = Label("id", "value")
    label.addCssClass("a", "b")
    label.removeCssClass("a")

    val remover = label.behaviors.last()
    val updated = invokeDeclared(remover, "update", mutableSetOf("a", "b", "c")) as Set<*>

    val panel = WebMarkupContainer("panel")
    val queued = panel.q(Label("child", "queued"))

    assertThat(updated).containsExactlyInAnyOrder("b", "c")
    assertThat(queued.id).isEqualTo("child")
  }

  @Test
  fun `dropdown companion and dm page unary plus are covered`() {
    val holder = Holder("a")
    val choice = DmDropDownChoice.of(holder::selected, Model.ofList(listOf("a", "b"))) {
      enable = { false }
      visible = { true }
    }

    invokeDeclared(choice, "onInitialize")
    invokeDeclared(choice, "onConfigure")

    val page = UnaryPlusPage()

    assertThat(choice.id).isEqualTo("selected")
    assertThat(choice.isEnabled).isFalse()
    assertThat(page.get("content")).isNotNull
    assertThat(page.behaviors).isNotEmpty
  }
}
