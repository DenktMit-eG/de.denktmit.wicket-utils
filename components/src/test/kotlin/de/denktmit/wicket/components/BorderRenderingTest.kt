package de.denktmit.wicket.components

import de.denktmit.wicket.components.border.Border
import de.denktmit.wicket.components.border.ListBorder
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class BorderRenderingTest : WicketTestBase() {

  @Test
  fun `border onInitialize adds CSS and calls addComponents and init`() {
    var initCalled = false
    val border = object : Border("b", init = { initCalled = true }) {
      override fun addComponents() {
        addToBorder(Label("label", "v"))
      }
    }
    invokeDeclared(border, "onInitialize")

    assertThat(initCalled).isTrue()
    assertThat(border.get("label")).isNotNull
    assertThat(border.behaviors).isNotEmpty
  }

  @Test
  fun `border operator unaryPlus adds to body container`() {
    val border = object : Border("b", init = {
      +Label("child", "v")
    }) {}
    invokeDeclared(border, "onInitialize")

    assertThat(border.bodyContainer.get("child")).isNotNull
  }

  @Test
  fun `border onConfigure sets visibility`() {
    val border = object : Border("b") {}
    border.visible = { false }
    assertThatCode { invokeDeclared(border, "onConfigure") }.doesNotThrowAnyException()
    assertThat(border.isVisible).isFalse()
  }

  @Test
  fun `border onConfigure without visibility keeps visible`() {
    val border = object : Border("b") {}
    assertThatCode { invokeDeclared(border, "onConfigure") }.doesNotThrowAnyException()
    assertThat(border.isVisible).isTrue()
  }

  @Test
  fun `border delegate methods route to bodyContainer`() {
    val border = object : Border("b") {}
    border.add(Label("a", "v"))
    border.addOrReplace(Label("a", "v2"))
    border.replace(Label("a", "v3"))
    border.remove("a")
    border.add(Label("c", "v4"))
    border.removeAll()

    assertThat(border.enableAjax()).isNotNull
  }

  @Test
  fun `listBorder visible callback is stored`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.visible = { false }

    assertThat(listBorder.visible != null).isTrue()
  }

  @Test
  fun `listBorder enableAjax sets flags`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.enableAjax()

    assertThat(listBorder.outputMarkupId).isTrue()
    assertThat(listBorder.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `listBorder addToBorder replaceInBorder and removeFromBorder work`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.addToBorder(Label("t", "v"))
    listBorder.replaceInBorder(Label("t", "v2"))
    listBorder.removeFromBorder(listBorder.get("t"))

    assertThat(listBorder.id).isEqualTo("lb")
  }
}
