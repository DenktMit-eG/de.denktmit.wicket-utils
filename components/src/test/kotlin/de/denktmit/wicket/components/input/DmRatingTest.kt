package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmRatingTest : InputTestBase() {

  @Test
  fun `rating is a radio group of symbols, lit up to the value, and submits a step`() {
    val model = Model<Double?>(3.0)
    val rating = renderInForm(DmRating("level", model, symbols = 5))
    assertHooks(rating)
    assertThat(tag("level").getAttribute("role")).isEqualTo("radiogroup")
    assertThat(tags("radio")).hasSize(5)
    assertThat(tags("steps").count { it.getAttribute("class").contains("dm-rating-lit") }).isEqualTo(3)
    assertStyleSheet("dm-rating")
    form().apply { select("level:group", 4); submit() }
    assertThat(model.`object`).isEqualTo(5.0)
  }

  @Test
  fun `half steps double the radios`() {
    val rating = renderInForm(DmRating("level", Model(2.5), symbols = 4, halfSteps = true))
    assertThat(rating.values).containsExactly(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0)
    assertThat(tags("steps").count { it.getAttribute("class").contains("dm-rating-half") }).isEqualTo(4)
    assertThat(rating.format(2.5)).isEqualTo("2.5 of 4")
  }

  @Test
  fun `read-only rating announces the value and keeps it on submit`() {
    val model = Model<Double?>(4.0)
    renderInForm(DmRating("level", model, readOnly = true))
    assertThat(tag("level").getAttribute("role")).isEqualTo("img")
    assertThat(tag("level").getAttribute("aria-label")).isEqualTo("Rating: 4 of 5")
    assertThat(html).containsPattern("<input[^>]*type=\"radio\"[^>]*disabled")
    form().submit()
    assertThat(model.`object`).isEqualTo(4.0)
  }
}
