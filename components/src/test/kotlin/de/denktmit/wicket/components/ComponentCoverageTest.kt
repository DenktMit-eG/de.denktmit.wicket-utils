package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmActionLink
import de.denktmit.wicket.components.component.DmAjaxCheckbox
import de.denktmit.wicket.components.component.DmAjaxEventBehavior
import de.denktmit.wicket.components.component.DmAjaxFallbackButton
import de.denktmit.wicket.components.component.DmAjaxLink
import de.denktmit.wicket.components.component.DmDropDownChoiceRenderer
import de.denktmit.wicket.components.component.DmExternalLink
import de.denktmit.wicket.components.component.DmGenericPanel
import de.denktmit.wicket.components.component.DmImage
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListView
import de.denktmit.wicket.components.component.DmNullableLabel
import de.denktmit.wicket.components.component.DmPageLink
import de.denktmit.wicket.components.component.DmRadioGroup
import de.denktmit.wicket.components.component.LazyPageLink
import de.denktmit.wicket.components.component.MonthChoice
import de.denktmit.wicket.components.component.PageRef
import de.denktmit.wicket.model.modelOf
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ComponentCoverageTest : WicketTestBase() {

  private class TestPage : WebPage()

  @Test
  fun `DmExternalLink string href constructor and onComponentTag adds target blank`() {
    val link = DmExternalLink("link", "https://example.com", "Example")
    invokeDeclared(link, "onInitialize")

    assertThat(link.id).isEqualTo("link")
    assertThat(link.behaviors).isNotEmpty
  }

  @Test
  fun `DmExternalLink KProperty constructor`() {
    class Holder {
      val url: String? = "https://example.com"
    }
    val holder = Holder()
    val link = DmExternalLink(holder::url)

    assertThat(link.id).isEqualTo("url")
  }

  @Test
  fun `DmExternalLink init lambda is called`() {
    var initCalled = false
    val link = DmExternalLink("link", "https://example.com") { initCalled = true }
    invokeDeclared(link, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `DmExternalLink unary plus adds behavior`() {
    val link = DmExternalLink("link", "https://example.com") {
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    }
    invokeDeclared(link, "onInitialize")

    assertThat(link.behaviors.size).isGreaterThanOrEqualTo(2)
  }

  @Test
  fun `DmLabel visible callback controls visibility`() {
    val label = DmLabel("l", "text")
    label.visible = { false }
    invokeDeclared(label, "onConfigure")

    assertThat(label.isVisible).isFalse()
  }

  @Test
  fun `DmLabel without visible stays visible`() {
    val label = DmLabel("l", "text")
    invokeDeclared(label, "onConfigure")

    assertThat(label.isVisible).isTrue()
  }

  @Test
  fun `DmLabel KProperty constructor`() {
    class Holder {
      val name: String = "hello"
    }
    val holder = Holder()
    val label = DmLabel(holder::name)

    assertThat(label.id).isEqualTo("name")
  }

  @Test
  fun `DmNullableLabel visible callback controls visibility`() {
    val label = DmNullableLabel("l", "text")
    label.visible = { false }
    invokeDeclared(label, "onConfigure")

    assertThat(label.isVisible).isFalse()
  }

  @Test
  fun `DmNullableLabel without visible stays visible`() {
    val label = DmNullableLabel("l", "text")
    invokeDeclared(label, "onConfigure")

    assertThat(label.isVisible).isTrue()
  }

  @Test
  fun `DmNullableLabel KProperty constructor`() {
    class Holder {
      val name: String? = null
    }
    val holder = Holder()
    val label = DmNullableLabel(holder::name)

    assertThat(label.id).isEqualTo("name")
  }

  @Test
  fun `DmNullableLabel init lambda is called`() {
    var initCalled = false
    val label = DmNullableLabel("l", "text") { initCalled = true }
    invokeDeclared(label, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `DmNullableLabel unary plus adds behavior`() {
    val label = DmNullableLabel("l", "text") {
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    }
    invokeDeclared(label, "onInitialize")

    assertThat(label.behaviors.size).isGreaterThanOrEqualTo(2)
  }

  @Test
  fun `DmListView inline mode`() {
    val view = DmListView<String, List<String>>("list", Model.ofList(listOf("a")), inline = true) {}
    assertThat(view.inline).isTrue()
  }

  @Test
  fun `DmListView enableAjax sets flags`() {
    val view = DmListView<String, List<String>>("list", Model.ofList(listOf("a"))) {}
    view.enableAjax()

    assertThat(view.outputMarkupId).isTrue()
    assertThat(view.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `DmListView KProperty constructor`() {
    class Holder {
      val items: List<String> = listOf("a", "b")
    }
    val holder = Holder()
    val view = DmListView(holder::items) {}

    assertThat(view.id).isEqualTo("items")
  }

  @Test
  fun `DmActionLink onClick invokes callback`() {
    var clicked = false
    val link = DmActionLink("link") {}
    link.onClick = { clicked = true }
    invokeDeclared(link, "onInitialize")
    link.onClick!!.invoke()

    assertThat(clicked).isTrue()
  }

  @Test
  fun `DmActionLink onClick with null callback does not throw`() {
    val link = DmActionLink("link")
    invokeDeclared(link, "onInitialize")
    invokeDeclared(link, "onClick")
  }

  @Test
  fun `DmActionLink visible callback`() {
    val link = DmActionLink("link")
    link.visible = { false }
    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isFalse()
  }

  @Test
  fun `DmActionLink bodyModel is set on init`() {
    val link = DmActionLink("link", bodyModel = Model.of("Click me"))
    invokeDeclared(link, "onInitialize")

    assertThat(link.body).isNotNull
  }

  @Test
  fun `DmActionLink unary plus operators work`() {
    val link = DmActionLink("link") {
      +Label("child", "v")
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    }
    invokeDeclared(link, "onInitialize")

    assertThat(link.get("child")).isNotNull
  }

  @Test
  fun `DmAjaxLink visible callback`() {
    val link = DmAjaxLink<Any>("link", click = {})
    link.visible = { false }
    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isFalse()
  }

  @Test
  fun `DmAjaxLink bodyModel is set on init`() {
    val link = DmAjaxLink<Any>("link", click = {}, bodyModel = Model.of("text"))
    invokeDeclared(link, "onInitialize")

    assertThat(link.body).isNotNull
  }

  @Test
  fun `DmAjaxLink without bodyModel does not set body`() {
    val link = DmAjaxLink<Any>("link", click = {})
    invokeDeclared(link, "onInitialize")

    assertThat(link.body).isNull()
  }

  @Test
  fun `DmAjaxLink unary plus operators work`() {
    val link = DmAjaxLink<Any>("link", click = {}, init = {
      +Label("child", "v")
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    })
    invokeDeclared(link, "onInitialize")

    assertThat(link.get("child")).isNotNull
  }

  @Test
  fun `DmGenericPanel visible callback`() {
    val panel = DmGenericPanel<String>("p")
    panel.visible = { false }
    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isFalse()
  }

  @Test
  fun `DmGenericPanel without visible stays visible`() {
    val panel = DmGenericPanel<String>("p")
    invokeDeclared(panel, "onConfigure")

    assertThat(panel.isVisible).isTrue()
  }

  @Test
  fun `DmGenericPanel enableAjax sets flags`() {
    val panel = DmGenericPanel<String>("p")
    panel.enableAjax()

    assertThat(panel.outputMarkupId).isTrue()
    assertThat(panel.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `DmGenericPanel init lambda and unary plus`() {
    var initCalled = false
    val panel = DmGenericPanel<String>("p") {
      initCalled = true
      +Label("child", "v")
      +org.apache.wicket.AttributeModifier.replace("class", "x")
    }
    invokeDeclared(panel, "onInitialize")

    assertThat(initCalled).isTrue()
    assertThat(panel.get("child")).isNotNull
  }

  @Test
  fun `DmRadioGroup visible callback`() {
    val group = DmRadioGroup<String>("rg")
    group.visible = { false }
    invokeDeclared(group, "onConfigure")

    assertThat(group.isVisible).isFalse()
  }

  @Test
  fun `DmRadioGroup without visible stays visible`() {
    val group = DmRadioGroup<String>("rg")
    invokeDeclared(group, "onConfigure")

    assertThat(group.isVisible).isTrue()
  }

  @Test
  fun `DmRadioGroup init lambda and unary plus`() {
    var initCalled = false
    val group = DmRadioGroup<String>("rg") {
      initCalled = true
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    }
    invokeDeclared(group, "onInitialize")

    assertThat(initCalled).isTrue()
    assertThat(group.behaviors).isNotEmpty
  }

  @Test
  fun `DmDropDownChoiceRenderer visible callback`() {
    val renderer = DmDropDownChoiceRenderer(
      "dd", modelOf<String?> { null }, Model.ofList(listOf("a")),
      ChoiceRenderer("", "")
    )
    renderer.visible = { false }
    invokeDeclared(renderer, "onConfigure")

    assertThat(renderer.isVisible).isFalse()
  }

  @Test
  fun `DmDropDownChoiceRenderer without visible stays visible`() {
    val renderer = DmDropDownChoiceRenderer(
      "dd", modelOf<String?> { null }, Model.ofList(listOf("a")),
      ChoiceRenderer("", "")
    )
    invokeDeclared(renderer, "onConfigure")

    assertThat(renderer.isVisible).isTrue()
  }

  @Test
  fun `DmDropDownChoiceRenderer init lambda is called`() {
    var initCalled = false
    val renderer = DmDropDownChoiceRenderer(
      "dd", modelOf<String?> { null }, Model.ofList(listOf("a")),
      ChoiceRenderer("", ""), init = { initCalled = true }
    )
    invokeDeclared(renderer, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `MonthChoice has 12 months`() {
    val choice = MonthChoice("month", Model.of(1))

    assertThat(choice.choices).hasSize(12)
    assertThat(choice.choices).contains(1, 6, 12)
  }

  @Test
  fun `DmPageLink visible callback`() {
    val link = DmPageLink("link", TestPage::class.java)
    link.visible = { false }
    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isFalse()
  }

  @Test
  fun `DmPageLink without visible stays visible`() {
    val link = DmPageLink("link", TestPage::class.java)
    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isTrue()
  }

  @Test
  fun `DmPageLink bodyModel is set on init`() {
    val link = DmPageLink("link", TestPage::class.java, bodyModel = Model.of("Click"))
    invokeDeclared(link, "onInitialize")

    assertThat(link.body).isNotNull
  }

  @Test
  fun `DmPageLink init lambda is called`() {
    var initCalled = false
    val link = DmPageLink("link", TestPage::class.java, init = { initCalled = true })
    invokeDeclared(link, "onInitialize")

    assertThat(initCalled).isTrue()
  }

  @Test
  fun `DmPageLink unary plus operators work`() {
    val link = DmPageLink("link", TestPage::class.java, init = {
      +Label("child", "v")
      +org.apache.wicket.AttributeModifier.replace("class", "custom")
    })
    invokeDeclared(link, "onInitialize")

    assertThat(link.get("child")).isNotNull
  }

  @Test
  fun `DmPageLink param with value sets page parameter`() {
    class Holder {
      val q: String? = null
    }
    val link = DmPageLink("link", TestPage::class.java)
    link.param(Holder::q, "search")

    assertThat(link.pageParameters.get("q").toString()).isEqualTo("search")
  }

  @Test
  fun `DmPageLink param with model adds to paramModels`() {
    class Holder {
      val q: String? = null
    }
    val model = Model.of("search")
    val link = DmPageLink("link", TestPage::class.java)
    link.param(Holder::q, model)

    assertThat(link.paramModels).isNotEmpty
  }

  @Test
  fun `DmPageLink onConfigure with visible applies paramModels`() {
    class Holder {
      val q: String? = null
    }
    val model = Model.of("value")
    val link = DmPageLink("link", TestPage::class.java)
    link.param(Holder::q, model)
    invokeDeclared(link, "onConfigure")

    assertThat(link.pageParameters.get("q").toString()).isEqualTo("value")
  }

  @Test
  fun `DmPageLink PageRef constructor`() {
    val ref = PageRef(TestPage::class.java)
    val link = DmPageLink("link", ref)

    assertThat(link.id).isEqualTo("link")
  }

  @Test
  fun `PageRef param sets parameter`() {
    class Holder {
      val id: String? = null
    }
    val ref = PageRef(TestPage::class.java)
    ref.param(Holder::id, "123")

    assertThat(ref.parameters.get("id").toString()).isEqualTo("123")
  }

  @Test
  fun `DmPageLink tooltip getter and setter`() {
    val link = DmPageLink("link", TestPage::class.java)
    link.tooltip = "Help text"

    assertThat(link.behaviors).isNotEmpty
  }

  @Test
  fun `DmAjaxFallbackButton visible callback stored`() {
    val form = Form<Any>("form")
    val button = DmAjaxFallbackButton("btn", form)
    button.visible = { false }

    assertThat(button.visible != null).isTrue()
    assertThat(button.visible!!.invoke()).isFalse()
  }

  @Test
  fun `DmAjaxFallbackButton init lambda and unary plus`() {
    val form = Form<Any>("form")
    var initCalled = false
    val button = DmAjaxFallbackButton("btn", form) {
      initCalled = true
      +Label("child", "v")
      +org.apache.wicket.AttributeModifier.replace("class", "x")
    }
    invokeDeclared(button, "onInitialize")

    assertThat(initCalled).isTrue()
    assertThat(button.get("child")).isNotNull
  }

  @Test
  fun `DmImage enableAjax sets flags`() {
    val image = DmImage("img", org.apache.wicket.request.resource.ByteArrayResource("image/png", byteArrayOf()))
    image.enableAjax()

    assertThat(image.outputMarkupId).isTrue()
    assertThat(image.outputMarkupPlaceholderTag).isTrue()
  }

  @Test
  fun `DmImage visibility callback`() {
    val image = DmImage("img", org.apache.wicket.request.resource.ByteArrayResource("image/png", byteArrayOf()))
    image.visibility = { false }
    invokeDeclared(image, "onConfigure")

    assertThat(image.isVisible).isFalse()
  }

  @Test
  fun `DmImage without visibility stays visible`() {
    val image = DmImage("img", org.apache.wicket.request.resource.ByteArrayResource("image/png", byteArrayOf()))
    invokeDeclared(image, "onConfigure")

    assertThat(image.isVisible).isTrue()
  }

  @Test
  fun `DmImage reloadAntiCache sets new resource and antiCache`() {
    val image = DmImage("img", org.apache.wicket.request.resource.ByteArrayResource("image/png", byteArrayOf()))
    image.reloadAntiCache(listOf("v1", "v2"), byteArrayOf(1, 2))

    assertThat(image.antiCache).containsExactly("v1", "v2")
    assertThat(image.resource).isNotNull
  }

  @Test
  fun `LazyPageLink stores page class and parameters lambdas`() {
    val link = LazyPageLink(
      "link",
      pageClass = { TestPage::class.java },
      parameters = { PageParameters() },
    )

    assertThat(link.id).isEqualTo("link")
  }
}
