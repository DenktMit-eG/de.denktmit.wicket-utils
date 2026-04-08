package de.denktmit.wicket.components

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BorderRenderIT : WicketTestBase() {

  @Test
  fun `border renders page with body content and border content`() {
    tester.startPage(TestRenderBorderPage::class.java)

    tester.assertRenderedPage(TestRenderBorderPage::class.java)
    tester.assertContains("border-label")
    tester.assertContains("inner-content")
  }

  @Test
  fun `border render exercises BorderBodyContainer lifecycle`() {
    val page = tester.startPage(TestRenderBorderPage::class.java)
    val border = page.get("border") as TestRenderBorder

    assertThat(border.bodyContainer).isNotNull
    assertThat(border.bodyContainer.get("inner")).isNotNull
  }

  @Test
  fun `border getMarkup for null child returns border markup fragment`() {
    val page = tester.startPage(TestRenderBorderPage::class.java)
    val border = page.get("border") as TestRenderBorder

    val markup = border.getMarkup(null)
    assertThat(markup).isNotNull
    assertThat(markup.toString()).contains("wicket:border")
  }

  @Test
  fun `border getMarkup for bodyContainer returns body markup`() {
    val page = tester.startPage(TestRenderBorderPage::class.java)
    val border = page.get("border") as TestRenderBorder

    val bodyMarkup = border.getMarkup(border.bodyContainer)
    assertThat(bodyMarkup).isNotNull
  }

  @Test
  fun `border getMarkup for border child finds markup in border fragment`() {
    val page = tester.startPage(TestRenderBorderPage::class.java)
    val border = page.get("border") as TestRenderBorder

    val labelMarkup = border.getMarkup(border.get("label"))
    assertThat(labelMarkup).isNotNull
  }

  @Test
  fun `border getRegionMarkup returns border fragment`() {
    val page = tester.startPage(TestRenderBorderPage::class.java)
    val border = page.get("border") as TestRenderBorder

    val regionMarkup = border.regionMarkup
    assertThat(regionMarkup).isNotNull
  }

  @Test
  fun `border bodyContainer getMarkup for child finds markup in page`() {
    val page = tester.startPage(TestRenderBorderPage::class.java)
    val border = page.get("border") as TestRenderBorder

    val innerMarkup = border.bodyContainer.getMarkup(border.bodyContainer.get("inner"))
    assertThat(innerMarkup).isNotNull
  }
}
