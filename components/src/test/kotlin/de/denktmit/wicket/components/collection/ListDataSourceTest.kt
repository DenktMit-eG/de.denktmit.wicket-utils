package de.denktmit.wicket.components.collection

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ListDataSourceTest {

  @Test
  fun `answers size and slices in the list's order without a sort`() {
    val source = source()
    assertThat(source.size()).isEqualTo(12)
    assertThat(source.iterator(0, 2).asSequence().map { it.name }.toList()).containsExactly("Mia", "Ben")
    assertThat(source.iterator(10, 5).asSequence().map { it.name }.toList()).containsExactly("Ivo", "Jo")
  }

  @Test
  fun `sorts by a registered comparator in both directions and ignores an unknown property`() {
    val source = source()
    source.sort = SortParam("name", true)
    assertThat(source.iterator(0, 3).asSequence().map { it.name }.toList()).containsExactly("Anna", "Ben", "Carl")
    source.sort = SortParam("rate", false)
    assertThat(source.iterator(0, 2).asSequence().map { it.name }.toList()).containsExactly("Anna", "Eve")
    source.sort = SortParam("team", true)
    assertThat(source.iterator(0, 1).asSequence().map { it.name }.toList()).containsExactly("Mia")
  }

  @Test
  fun `an empty list is a valid source`() {
    val source = ListDataSource<Member, String>(emptyList())
    assertThat(source.size()).isZero()
    assertThat(source.iterator(0, 10).hasNext()).isFalse()
  }
}
