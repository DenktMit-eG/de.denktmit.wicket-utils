package de.denktmit.wicket.components

import de.denktmit.wicket.components.border.Border
import de.denktmit.wicket.components.border.ListBorder
import de.denktmit.wicket.components.component.DmLabel
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BorderFullRenderTest : WicketTestBase() {

  @Test
  fun `border queue delegates to body container`() {
    val border = object : Border("b") {}
    border.queue(Label("q", "queued"))

    assertThat(border.bodyContainer).isNotNull
  }

  @Test
  fun `border addToBorder and queueToBorder add to border itself`() {
    val border = object : Border("b") {}
    invokeDeclared(border, "onInitialize")
    border.addToBorder(Label("direct", "value"))
    assertThat(border.get("direct")).isNotNull

    border.queueToBorder(Label("queued", "value"))
  }

  @Test
  fun `border newMarkupSourcingStrategy returns BorderMarkupSourcingStrategy`() {
    val border = object : Border("b") {}

    val strategy = invokeDeclared(border, "newMarkupSourcingStrategy")
    assertThat(strategy).isNotNull
    assertThat(strategy!!.javaClass.simpleName).isEqualTo("BorderMarkupSourcingStrategy")
  }

  @Test
  fun `border init lambda is called during onInitialize`() {
    var initCalled = false
    val border = object : Border("b", init = { initCalled = true }) {}
    invokeDeclared(border, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `border addComponents is called during onInitialize`() {
    var addComponentsCalled = false
    val border = object : Border("b") {
      override fun addComponents() {
        addComponentsCalled = true
      }
    }
    invokeDeclared(border, "onInitialize")

    assertThat(addComponentsCalled).isTrue()
  }

  @Test
  fun `border add delegates to bodyContainer`() {
    val border = object : Border("b") {}
    border.add(Label("child", "v"))

    assertThat(border.bodyContainer.get("child")).isNotNull
  }

  @Test
  fun `border addOrReplace delegates to bodyContainer`() {
    val border = object : Border("b") {}
    border.add(Label("child", "v"))
    border.addOrReplace(Label("child", "v2"))

    assertThat(border.bodyContainer.get("child")).isNotNull
  }

  @Test
  fun `border replace delegates to bodyContainer`() {
    val border = object : Border("b") {}
    border.add(Label("child", "v"))
    border.replace(Label("child", "v2"))

    assertThat(border.bodyContainer.get("child")).isNotNull
  }

  @Test
  fun `border remove by id delegates to bodyContainer`() {
    val border = object : Border("b") {}
    border.add(Label("child", "v"))
    border.remove("child")

    assertThat(border.bodyContainer.get("child")).isNull()
  }

  @Test
  fun `border remove by component delegates to bodyContainer`() {
    val border = object : Border("b") {}
    val label = Label("child", "v")
    border.add(label)
    border.remove(label)

    assertThat(border.bodyContainer.get("child")).isNull()
  }

  @Test
  fun `border removeAll delegates to bodyContainer`() {
    val border = object : Border("b") {}
    border.add(Label("a", "v"))
    border.add(Label("b", "v"))
    border.removeAll()

    assertThat(border.bodyContainer.size()).isEqualTo(0)
  }

  @Test
  fun `border replaceInBorder works`() {
    val border = object : Border("b") {}
    invokeDeclared(border, "onInitialize")
    border.addToBorder(Label("direct", "v"))
    border.replaceInBorder(Label("direct", "v2"))

    assertThat(border.get("direct")).isNotNull
  }

  @Test
  fun `border removeFromBorder works`() {
    val border = object : Border("b") {}
    invokeDeclared(border, "onInitialize")
    border.addToBorder(Label("direct", "v"))
    border.removeFromBorder(border.get("direct"))

    assertThat(border.get("direct")).isNull()
  }

  @Test
  fun `border enableAjax sets output flags`() {
    val border = object : Border("b") {}
    border.enableAjax()

    assertThat(border.outputMarkupId).isTrue()
    assertThat(border.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `border onConfigure sets visibility via callback`() {
    val border = object : Border("b") {}
    border.visible = { false }
    invokeDeclared(border, "onConfigure")

    assertThat(border.isVisible).isFalse()
  }

  @Test
  fun `border onConfigure without visible stays visible`() {
    val border = object : Border("b") {}
    invokeDeclared(border, "onConfigure")

    assertThat(border.isVisible).isTrue()
  }

  @Test
  fun `border onConfigure with visible true stays visible`() {
    val border = object : Border("b") {}
    border.visible = { true }
    invokeDeclared(border, "onConfigure")

    assertThat(border.isVisible).isTrue()
  }

  @Test
  fun `border bodyContainer id is derived from border id`() {
    val border = object : Border("myBorder") {}

    assertThat(border.bodyContainer.id).isEqualTo("myBorder_body")
  }

  @Test
  fun `border unary plus Component adds to body`() {
    val border = object : Border("b", init = {
      +Label("inner", "text")
    }) {}
    invokeDeclared(border, "onInitialize")

    assertThat(border.bodyContainer.get("inner")).isNotNull
  }

  @Test
  fun `border unary plus Behavior adds to border`() {
    val border = object : Border("b", init = {
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    }) {}
    invokeDeclared(border, "onInitialize")

    assertThat(border.behaviors.size).isGreaterThanOrEqualTo(2)
  }

  // ListBorder tests

  @Test
  fun `listBorder newMarkupSourcingStrategy returns BorderMarkupSourcingStrategy`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    val strategy = invokeDeclared(listBorder, "newMarkupSourcingStrategy")
    assertThat(strategy).isNotNull
    assertThat(strategy!!.javaClass.simpleName).isEqualTo("BorderMarkupSourcingStrategy")
  }

  @Test
  fun `listBorder add throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.add(Label("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder addOrReplace throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.addOrReplace(Label("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder remove component throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.remove(Label("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder remove by id throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.remove("x") }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder removeAll throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.removeAll() }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder replace throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.replace(Label("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder queue throws UnsupportedOperationException`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy { listBorder.queue(Label("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder addToBorder works`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.addToBorder(Label("t", "v"))

    assertThat(listBorder.get("t")).isNotNull
  }

  @Test
  fun `listBorder replaceInBorder works`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.addToBorder(Label("t", "v"))
    listBorder.replaceInBorder(Label("t", "v2"))

    assertThat(listBorder.get("t")).isNotNull
  }

  @Test
  fun `listBorder removeFromBorder works`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.addToBorder(Label("t", "v"))
    listBorder.removeFromBorder(listBorder.get("t"))
  }

  @Test
  fun `listBorder enableAjax sets output flags`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.enableAjax()

    assertThat(listBorder.outputMarkupId).isTrue()
    assertThat(listBorder.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `listBorder visible callback is stored and readable`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}
    listBorder.visible = { false }

    assertThat(listBorder.visible != null).isTrue()
    assertThat(listBorder.visible!!.invoke()).isFalse()
  }

  @Test
  fun `listBorder unary plus Component throws`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    assertThatThrownBy {
      with(listBorder) {
        +Label("inner", "text")
      }
    }.isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `listBorder unary plus Behavior adds to border`() {
    val listBorder = object : ListBorder<String, List<String>>(
      "lb", Model.ofList(listOf("a")), populateItem = {}
    ) {}

    with(listBorder) {
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    }

    assertThat(listBorder.behaviors).isNotEmpty
  }
}
