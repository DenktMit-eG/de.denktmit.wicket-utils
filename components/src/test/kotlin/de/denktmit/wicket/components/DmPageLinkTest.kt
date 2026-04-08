package de.denktmit.wicket.components

import de.denktmit.wicket.components.component.DmPageLink
import de.denktmit.wicket.components.component.PageRef
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmPageLinkTest : WicketTestBase() {
  private data class QueryHolder(var q: String? = null)

  @Test
  fun `constructs with page class`() {
    val link = DmPageLink("pl", WrapperTestPage::class.java)

    assertThat(link.pageClass).isEqualTo(WrapperTestPage::class.java)
  }

  @Test
  fun `onConfigure copies dynamic parameters when visible`() {
    val link = DmPageLink("pl", WrapperTestPage::class.java)
    val holder = QueryHolder()
    link.param(holder::q, Model.of("query"))

    invokeDeclared(link, "onConfigure")

    assertThat(link.pageParameters.get("q").toString()).isEqualTo("query")
  }

  @Test
  fun `supports page ref constructor and tooltip`() {
    val pageRef = PageRef(WrapperTestPage::class.java)
    val link = DmPageLink("pl", pageRef)
    link.tooltip = "Title"

    assertThat(link.pageClass).isEqualTo(WrapperTestPage::class.java)
    assertThat(link.behaviors).isNotEmpty
  }

  @Test
  fun `onConfigure does not copy model params when hidden`() {
    val link = DmPageLink("pl", WrapperTestPage::class.java)
    val holder = QueryHolder()
    link.param(holder::q, Model.of("query"))
    link.visible = { false }

    invokeDeclared(link, "onConfigure")

    assertThat(link.isVisible).isFalse()
    assertThat(link.pageParameters.get("q").toOptionalString()).isNull()
  }

  @Test
  fun `onInitialize sets bodyModel and CSS`() {
    val link = DmPageLink("pl", WrapperTestPage::class.java, bodyModel = Model.of("text"))

    invokeDeclared(link, "onInitialize")
  }

  @Test
  fun `static param writes page parameter`() {
    val link = DmPageLink("pl", WrapperTestPage::class.java)
    val holder = QueryHolder()
    link.param(holder::q, "value")

    assertThat(link.pageParameters.get("q").toString()).isEqualTo("value")
  }

  @Test
  fun `PageRef param stores parameter`() {
    val holder = QueryHolder()
    val ref = PageRef(WrapperTestPage::class.java)
    ref.param(holder::q, "value")

    assertThat(ref.parameters.get("q").toString()).isEqualTo("value")
  }
}
