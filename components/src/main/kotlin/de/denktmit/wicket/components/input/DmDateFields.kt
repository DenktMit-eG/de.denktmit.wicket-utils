package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.widget.widgetHooks
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.validation.IValidatable
import org.apache.wicket.validation.IValidator
import org.apache.wicket.validation.ValidationError
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.IsoFields
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * The base of the date and time inputs (requirement E.41): a native input of one `type` whose calendar popup,
 * keyboard entry in the user's format and ISO submission are the browser's own, with minimum and maximum as the
 * browser's constraints and as server-side validation.
 */
abstract class DmTemporalField<T : Comparable<T>>(
  id: String,
  model: IModel<T?>?,
  private val type: Class<T>,
  private val inputType: String,
  private val min: T?,
  private val max: T?,
  @Transient
  private val parse: (String) -> T,
  @Transient
  private val format: (T) -> String,
) : TextField<T>(id, model, type) {

  @Suppress("UNCHECKED_CAST")
  override fun <C : Any?> getConverter(clazz: Class<C>): IConverter<C> = IsoConverter(type, parse, format) as IConverter<C>

  override fun onInitialize() {
    super.onInitialize()
    widgetHooks()
    if (min != null || max != null) {
      add(object : IValidator<T> {
        override fun validate(validatable: IValidatable<T>) {
          val value = validatable.value
          if (min != null && value < min) validatable.error(ValidationError().addKey("minimum").setVariable("minimum", format(min)))
          if (max != null && value > max) validatable.error(ValidationError().addKey("maximum").setVariable("maximum", format(max)))
        }
      })
    }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    checkComponentTag(tag, "input")
    tag.put("type", inputType)
    min?.let { tag.put("min", format(it)) }
    max?.let { tag.put("max", format(it)) }
  }
}

/** Validates against a rule that disables single dates (requirement E.41: disabled dates). */
class DisabledDatesValidator(
  @Transient
  private val disabled: (LocalDate) -> Boolean,
) : IValidator<LocalDate> {
  override fun validate(validatable: IValidatable<LocalDate>) {
    if (disabled(validatable.value)) validatable.error(ValidationError().addKey("DisabledDatesValidator").setVariable("date", validatable.value.toString()))
  }
}

/** A date (requirement E.41): `input type="date"`, a `LocalDate` model, optional bounds and disabled dates. */
open class DmDateField
@JvmOverloads
constructor(
  id: String,
  model: IModel<LocalDate?>?,
  min: LocalDate? = null,
  max: LocalDate? = null,
  disabled: ((LocalDate) -> Boolean)? = null,
) : DmTemporalField<LocalDate>(id, model, LocalDate::class.java, "date", min, max, LocalDate::parse, LocalDate::toString) {
  init {
    disabled?.let { add(DisabledDatesValidator(it)) }
  }
}

/** A time of day (requirement E.41): `input type="time"`, a `LocalTime` model, optional bounds and a step in seconds. */
open class DmTimeField
@JvmOverloads
constructor(
  id: String,
  model: IModel<LocalTime?>?,
  min: LocalTime? = null,
  max: LocalTime? = null,
  private val stepSeconds: Int? = null,
) : DmTemporalField<LocalTime>(id, model, LocalTime::class.java, "time", min, max, LocalTime::parse, { it.format(DateTimeFormatter.ofPattern("HH:mm:ss")) }) {
  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    stepSeconds?.let { tag.put("step", it.toString()) }
  }
}

/**
 * A date with a time (requirement E.41): `input type="datetime-local"`, a `LocalDateTime` model; [zone] is shown
 * next to the input where it matters, because the input itself has no zone.
 */
open class DmDateTimeField
@JvmOverloads
constructor(
  id: String,
  model: IModel<LocalDateTime?>?,
  min: LocalDateTime? = null,
  max: LocalDateTime? = null,
  /** The zone the value is read in, announced in the input's title and shown by the form field; `null` for none. */
  val zone: ZoneId? = null,
) : DmTemporalField<LocalDateTime>(id, model, LocalDateTime::class.java, "datetime-local", min, max, LocalDateTime::parse, { it.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) }) {
  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    zone?.let { tag.put("title", zoneLabel(locale)) }
  }

  /** The zone as the user reads it, for the page to show next to the input. */
  fun zoneLabel(locale: Locale): String = zone?.getDisplayName(TextStyle.SHORT, locale) ?: ""
}

/** A month (requirement E.41): `input type="month"`, a `YearMonth` model. */
open class DmMonthField
@JvmOverloads
constructor(
  id: String,
  model: IModel<YearMonth?>?,
  min: YearMonth? = null,
  max: YearMonth? = null,
) : DmTemporalField<YearMonth>(id, model, YearMonth::class.java, "month", min, max, YearMonth::parse, YearMonth::toString)

/** A year (requirement E.41): a number input with a `Year` model. */
open class DmYearField
@JvmOverloads
constructor(
  id: String,
  model: IModel<Year?>?,
  min: Year? = null,
  max: Year? = null,
) : DmTemporalField<Year>(id, model, Year::class.java, "number", min, max, { Year.of(it.toInt()) }, { it.value.toString() }) {
  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("step", "1")
  }
}

/** An ISO week: the week-based year and the week of that year, as `input type="week"` submits it (`2026-W12`). */
data class IsoWeek(val year: Int, val week: Int) : Comparable<IsoWeek>, java.io.Serializable {
  override fun compareTo(other: IsoWeek): Int = compareValuesBy(this, other, { it.year }, { it.week })

  override fun toString(): String = "%04d-W%02d".format(year, week)

  /** The Monday of this week. */
  fun start(): LocalDate = LocalDate.of(year, 1, 4).with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong()).with(WeekFields.ISO.dayOfWeek(), 1)

  companion object {
    /** Parses `2026-W12`. */
    @JvmStatic
    fun parse(text: String): IsoWeek {
      val match = Regex("(\\d{4})-W(\\d{1,2})").matchEntire(text) ?: throw java.time.format.DateTimeParseException("not an ISO week", text, 0)
      val week = IsoWeek(match.groupValues[1].toInt(), match.groupValues[2].toInt())
      if (week.week !in 1..53) throw java.time.format.DateTimeParseException("week out of range", text, 0)
      return week
    }

    /** The week of [date]. */
    @JvmStatic
    fun of(date: LocalDate): IsoWeek = IsoWeek(date.get(IsoFields.WEEK_BASED_YEAR), date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
  }
}

/** A week (requirement E.41): `input type="week"`, an [IsoWeek] model. */
open class DmWeekField
@JvmOverloads
constructor(
  id: String,
  model: IModel<IsoWeek?>?,
  min: IsoWeek? = null,
  max: IsoWeek? = null,
) : DmTemporalField<IsoWeek>(id, model, IsoWeek::class.java, "week", min, max, IsoWeek::parse, IsoWeek::toString)
