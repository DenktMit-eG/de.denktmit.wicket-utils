package de.denktmit.wicket.components.navigation

import de.denktmit.wicket.components.collection.DmSelectionModel
import de.denktmit.wicket.components.collection.SelectionMode
import de.denktmit.wicket.components.widget.WidgetTestBase
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmTreeTest : WidgetTestBase() {

  private val children = mapOf("root" to listOf("a", "b"), "a" to listOf("a1", "a2"))

  private fun provider() = object : ITreeProvider<String> {
    override fun getRoots(): Iterator<String> = listOf("root").iterator()

    override fun hasChildren(node: String): Boolean = children.containsKey(node)

    override fun getChildren(node: String): Iterator<String> = (children[node] ?: emptyList()).iterator()

    override fun model(node: String): IModel<String> = Model.of(node)

    override fun detach() {}
  }

  @Test
  fun `roots render collapsed, a toggle expands by link and the icons and labels show`() {
    val tree = render(DmTree("tree", provider(), icon = { if (children.containsKey(it)) "folder" else "file" }))
    assertHooks(tree)
    assertThat(tag("tree").getAttribute("role")).isEqualTo("tree")
    assertThat(tags("label").map { it.value }).containsExactly("root")
    assertThat(tags("toggle")).hasSize(1)
    assertThat(tester.getTagByWicketId("check")).isNull()
    assertStyleSheet("dm-tree")
    assertScript("dm-tree")

    tester.clickLink(tree.visitChildren(org.apache.wicket.markup.html.link.AbstractLink::class.java, org.apache.wicket.util.visit.IVisitor<org.apache.wicket.markup.html.link.AbstractLink, String> { c, v -> if (c.id == "toggle") v.stop(c.pageRelativePath) }))
    assertThat(tags("label").map { it.value }).containsExactly("root", "a", "b")
    assertThat(tree.getState("root")).isEqualTo(org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree.State.EXPANDED)
  }

  @Test
  fun `selection of one through the label link`() {
    val selection = DmSelectionModel<String>(SelectionMode.SINGLE)
    val tree = DmTree("tree", provider(), selection)
    render(tree)
    tree.expand("root")
    renderAgain()
    val path = tree.visitChildren(org.apache.wicket.markup.html.link.AbstractLink::class.java, org.apache.wicket.util.visit.IVisitor<org.apache.wicket.markup.html.link.AbstractLink, String> { c, v -> if (c.id == "select" && c.parent.defaultModelObject == "b") v.stop(c.pageRelativePath) })
    tester.clickLink(path)
    assertThat(selection.single).isEqualTo("b")
    assertThat(html).contains("aria-selected=\"true\"")
  }

  @Test
  fun `check boxes are tri-state over the known children`() {
    val tree = DmTree("tree", provider(), checkable = true)
    tree.check("a1", true)
    assertThat(tree.checkState("a")).isNull()
    assertThat(tree.checkState("root")).isNull()
    tree.check("a", true)
    assertThat(tree.checked).contains("a", "a1", "a2")
    assertThat(tree.checkState("a")).isTrue()
    tree.check("root", false)
    assertThat(tree.checked).isEmpty()
    assertThat(tree.checkState("root")).isFalse()
    render(tree)
    assertThat(tag("check").getAttribute("type")).isEqualTo("checkbox")
  }

  @Test
  fun `a drop is reported with the nodes behind the keys`() {
    var dropped: Pair<String, String>? = null
    val tree = DmTree("tree", provider(), draggable = true).apply { onDrop = { d, t, _ -> dropped = d to t } }
    render(tree)
    tree.expand("root")
    renderAgain()
    assertThat(tag("tree").getAttribute("data-dm-tree-drop")).contains("Wicket.Ajax")
    assertThat(html).contains("draggable=\"true\"")
    val behavior = tree.behaviors.filterIsInstance<org.apache.wicket.ajax.AbstractDefaultAjaxBehavior>().single()
    tester.request.setParameter("dragged", "b")
    tester.request.setParameter("target", "a")
    tester.executeBehavior(behavior)
    assertThat(dropped).isEqualTo("b" to "a")
  }

  @Test
  fun `tree table renders a treegrid over the same provider`() {
    val sortable = object : org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider<String, String>, ITreeProvider<String> by provider() {
      private val state = org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState<String>()
      override fun getSortState() = state
    }
    val columns = listOf<org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn<String, String>>(
      org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn(Model.of("Node")),
      org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn(Model.of("Length"), "length"),
    )
    val table = render(DmTreeTable("tt", columns, sortable))
    assertHooks(table)
    assertThat(tag("tt").getAttribute("role")).isEqualTo("treegrid")
    assertThat(html).contains("Node").contains("Length").contains("root")
    assertStyleSheet("dm-table")
  }
}
