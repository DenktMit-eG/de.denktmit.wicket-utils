package de.denktmit.wicket.components.layout

/** A step on the toolbox's modular spacing scale (`--s-3` to `--s5` in the base sheet). */
enum class Space(
  /** The custom property that holds the step. */
  val token: String,
) {
  S_3("--s-3"), S_2("--s-2"), S_1("--s-1"), S0("--s0"), S1("--s1"), S2("--s2"), S3("--s3"), S4("--s4"), S5("--s5"), NONE("0");

  /** The CSS value: `var(--s1)` or `0`. */
  val css: String get() = if (this == NONE) "0" else "var($token)"
}

/** Cross-axis alignment of children in a box. */
enum class Align(val css: String) { START("flex-start"), CENTER("center"), END("flex-end"), STRETCH("stretch"), BASELINE("baseline") }

/** Main-axis distribution of children in a box or toolbar. */
enum class Justify(val css: String) { START("flex-start"), CENTER("center"), END("flex-end"), BETWEEN("space-between"), AROUND("space-around") }

/** The side of a container a part sits on. */
enum class Side { TOP, BOTTOM, START, END }

/** Row or column. */
enum class Orientation { HORIZONTAL, VERTICAL }
