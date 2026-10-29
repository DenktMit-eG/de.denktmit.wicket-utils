package de.denktmit.wicket.components.collection

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmSelectionModelTest {

  @Test
  fun `single mode keeps one row and reports every change`() {
    val changes = mutableListOf<Set<String>>()
    val selection = DmSelectionModel<String>(SelectionMode.SINGLE).apply { onChange = { s, _ -> changes.add(s.toSet()) } }
    selection.select("a")
    selection.select("b")
    assertThat(selection.selected).containsExactly("b")
    assertThat(selection.single).isEqualTo("b")
    selection.toggle("b")
    assertThat(selection.selected).isEmpty()
    assertThat(changes).containsExactly(setOf("a"), setOf("b"), emptySet())
  }

  @Test
  fun `multi mode keeps many in order, set replaces, clear empties once`() {
    val selection = DmSelectionModel<String>(SelectionMode.MULTI, listOf("x"))
    selection.select("b")
    selection.select("a")
    assertThat(selection.selected).containsExactly("x", "b", "a")
    selection.set(listOf("c", "d"))
    assertThat(selection.selected).containsExactly("c", "d")
    var calls = 0
    selection.onChange = { _, _ -> calls++ }
    selection.clear()
    selection.clear()
    assertThat(calls).isEqualTo(1)
  }

  @Test
  fun `none mode selects nothing`() {
    val selection = DmSelectionModel<String>(SelectionMode.NONE)
    selection.select("a")
    selection.set(listOf("a", "b"))
    assertThat(selection.selected).isEmpty()
    assertThat(selection.isSelected("a")).isFalse()
  }
}
