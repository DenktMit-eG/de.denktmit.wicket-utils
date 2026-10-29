package de.denktmit.wicket.components.input

import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.util.Locale

class DmDateFieldsTest : InputTestBase() {

  private val input = "<input wicket:id=\"d\"/>"

  @Test
  fun `date field renders ISO with bounds and reads ISO back whatever the locale`() {
    tester.session.locale = Locale.GERMANY
    val model = Model<LocalDate?>(LocalDate.of(2026, 10, 29))
    val field = renderInForm(DmDateField("d", model, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)), input)
    assertHooks(field)
    assertThat(tag("d").getAttribute("type")).isEqualTo("date")
    assertThat(tag("d").getAttribute("value")).isEqualTo("2026-10-29")
    assertThat(tag("d").getAttribute("min")).isEqualTo("2026-01-01")
    assertThat(tag("d").getAttribute("max")).isEqualTo("2026-12-31")
    form().apply { setValue("d", "2026-03-05"); submit() }
    assertThat(model.`object`).isEqualTo(LocalDate.of(2026, 3, 5))
  }

  @Test
  fun `a date outside the bounds or on a disabled day is rejected`() {
    val model = Model<LocalDate?>(null)
    renderInForm(DmDateField("d", model, max = LocalDate.of(2026, 12, 31), disabled = { it.dayOfWeek.value >= 6 }), input)
    form().apply { setValue("d", "2027-01-01"); submit() }
    assertThat(model.`object`).isNull()
    tester.assertErrorMessages("The value must not be after 2026-12-31.")

    tester.clearFeedbackMessages()
    form().apply { setValue("d", "2026-10-31"); submit() }
    assertThat(model.`object`).isNull()
    tester.assertErrorMessages("The date 2026-10-31 cannot be chosen.")
  }

  @Test
  fun `unparseable text is a conversion error, not an exception`() {
    val model = Model<LocalDate?>(null)
    renderInForm(DmDateField("d", model), input)
    form().apply { setValue("d", "yesterday"); submit() }
    assertThat(model.`object`).isNull()
    assertThat(tester.getMessages(org.apache.wicket.feedback.FeedbackMessage.ERROR)).isNotEmpty()
  }

  @Test
  fun `time, date-time, month, year and week fields`() {
    val time = Model<LocalTime?>(LocalTime.of(9, 30))
    renderInForm(DmTimeField("d", time, stepSeconds = 60), input)
    assertThat(tag("d").getAttribute("type")).isEqualTo("time")
    assertThat(tag("d").getAttribute("value")).isEqualTo("09:30:00")
    assertThat(tag("d").getAttribute("step")).isEqualTo("60")
    form().apply { setValue("d", "17:45"); submit() }
    assertThat(time.`object`).isEqualTo(LocalTime.of(17, 45))

    val dateTime = Model<LocalDateTime?>(null)
    val dt = renderInForm(DmDateTimeField("d", dateTime, zone = ZoneId.of("Europe/Berlin")), input)
    assertThat(tag("d").getAttribute("type")).isEqualTo("datetime-local")
    assertThat(dt.zoneLabel(Locale.ENGLISH)).isNotBlank()
    form().apply { setValue("d", "2026-10-29T08:15"); submit() }
    assertThat(dateTime.`object`).isEqualTo(LocalDateTime.of(2026, 10, 29, 8, 15))

    val month = Model<YearMonth?>(YearMonth.of(2026, 10))
    renderInForm(DmMonthField("d", month), input)
    assertThat(tag("d").getAttribute("type")).isEqualTo("month")
    assertThat(tag("d").getAttribute("value")).isEqualTo("2026-10")
    form().apply { setValue("d", "2027-02"); submit() }
    assertThat(month.`object`).isEqualTo(YearMonth.of(2027, 2))

    val year = Model<Year?>(Year.of(2026))
    renderInForm(DmYearField("d", year, Year.of(2000), Year.of(2100)), input)
    assertThat(tag("d").getAttribute("type")).isEqualTo("number")
    assertThat(tag("d").getAttribute("value")).isEqualTo("2026")
    form().apply { setValue("d", "2031"); submit() }
    assertThat(year.`object`).isEqualTo(Year.of(2031))

    val week = Model<IsoWeek?>(IsoWeek(2026, 44))
    renderInForm(DmWeekField("d", week), input)
    assertThat(tag("d").getAttribute("type")).isEqualTo("week")
    assertThat(tag("d").getAttribute("value")).isEqualTo("2026-W44")
    form().apply { setValue("d", "2027-W02"); submit() }
    assertThat(week.`object`).isEqualTo(IsoWeek(2027, 2))
  }

  @Test
  fun `iso week parses, orders and knows its monday`() {
    assertThat(IsoWeek.parse("2026-W01")).isEqualTo(IsoWeek(2026, 1))
    assertThat(IsoWeek.of(LocalDate.of(2026, 10, 29))).isEqualTo(IsoWeek(2026, 44))
    assertThat(IsoWeek(2026, 44).start()).isEqualTo(LocalDate.of(2026, 10, 26))
    assertThat(IsoWeek(2026, 2)).isGreaterThan(IsoWeek(2026, 1)).isLessThan(IsoWeek(2027, 1))
    assertThatThrownBy { IsoWeek.parse("2026-W60") }.isInstanceOf(java.time.format.DateTimeParseException::class.java)
    assertThatThrownBy { IsoWeek.parse("week 3") }.isInstanceOf(java.time.format.DateTimeParseException::class.java)
  }
}
