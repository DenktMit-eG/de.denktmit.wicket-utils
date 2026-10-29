package de.denktmit.wicket.components.widget

import de.denktmit.wicket.components.behavior.cssClassIf
import de.denktmit.wicket.components.behavior.enabledIf
import de.denktmit.wicket.components.behavior.readOnlyIf
import de.denktmit.wicket.components.behavior.visibleIf
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RulesTest : WidgetTestBase() {

  @Test
  fun `visibility follows the rule on every render`() {
    var shown = true
    val box = render(WebMarkupContainer("box").visibleIf { shown })
    assertThat(tester.getTagByWicketId("box")).isNotNull

    shown = false
    renderAgain()
    assertThat(tester.getTagByWicketId("box")).isNull()
    assertThat(box.isVisible).isFalse()
  }

  @Test
  fun `enabled state follows the rule`() {
    var on = false
    val form = Form<Unit>("form")
    form.add(TextField("name", Model.of("x")).enabledIf { on })
    render(form, "<form wicket:id=\"form\"><input wicket:id=\"name\" type=\"text\"/></form>")
    assertThat(tag("name").getAttribute("disabled")).isNotNull
    on = true
    renderAgain()
    assertThat(tag("name").getAttribute("disabled")).isNull()
  }

  @Test
  fun `read-only state follows the rule`() {
    val form = Form<Unit>("form")
    form.add(TextField("name", Model.of("x")).readOnlyIf { true })
    render(form, "<form wicket:id=\"form\"><input wicket:id=\"name\" type=\"text\"/></form>")
    assertThat(tag("name").getAttribute("readonly")).isNotNull
  }

  @Test
  fun `a css class comes and goes with the rule`() {
    var active = true
    render(WebMarkupContainer("box").cssClassIf("active") { active })
    assertThat(tag("box").getAttribute("class")).contains("active")
    active = false
    renderAgain()
    assertThat(tag("box").getAttribute("class") ?: "").doesNotContain("active")
  }
}
