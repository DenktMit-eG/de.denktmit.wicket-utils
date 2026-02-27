package de.denktmit.wicket.components

import de.denktmit.wicket.components.border.Border
import de.denktmit.wicket.components.border.DmAutoCompleteDropdownPanel
import de.denktmit.wicket.components.border.ListBorder
import de.denktmit.wicket.components.border.MultiSelectBorder
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListItem
import de.denktmit.wicket.components.component.createPatternValidatedField
import de.denktmit.wicket.components.component.createPropertyValidatedCheckBox
import de.denktmit.wicket.components.component.createPropertyValidatedEmailField
import de.denktmit.wicket.components.component.createPropertyValidatedNumberTextField
import de.denktmit.wicket.components.component.createPropertyValidatedTextArea
import de.denktmit.wicket.components.component.createPropertyValidatedTextField
import de.denktmit.wicket.components.feedback.DmFeedbackPanel
import de.denktmit.wicket.components.page.MountPage
import de.denktmit.wicket.components.page.PageParams
import de.denktmit.wicket.components.table.DmGroupTableContainer
import de.denktmit.wicket.components.table.DmTableContainer
import de.denktmit.wicket.components.table.DynamicTableColumn
import de.denktmit.wicket.components.table.DynamicTableContainer
import de.denktmit.wicket.components.table.TableColumn
import de.denktmit.wicket.components.table.TableRowData
import de.denktmit.wicket.components.validator.DmStringValidator
import de.denktmit.wicket.components.validator.MailValidator
import org.apache.wicket.feedback.FeedbackMessage
import org.apache.wicket.model.Model
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ContainersAndValidationTest : WicketTestBase() {

  @MountPage(path = "/test", deployment = false)
  private class AnnotatedPage

  class Row(var name: String) : TableRowData<Row>

  private class TestBorder(id: String) : Border(id) {
    override fun addComponents() {
      +DmLabel("label", "value")
    }
  }

  private class TestListBorder(id: String) : ListBorder<String, List<String>>(id, Model.ofList(listOf("a")), populateItem = {})

  private class TestPageParams : PageParams {
    override val pp = PageParameters()
  }

  private class ExposedFeedbackPanel(id: String) : DmFeedbackPanel(id) {
    fun css(message: FeedbackMessage): String = getCSSClass(message)
  }

  private fun invokeProtected(target: Any, name: String, vararg args: Any?): Any? {
    var current: Class<*>? = target::class.java
    var method = current?.declaredMethods?.firstOrNull { it.name == name && it.parameterCount == args.size }
    while (method == null && current != null) {
      current = current.superclass
      method = current?.declaredMethods?.firstOrNull { it.name == name && it.parameterCount == args.size }
    }
    requireNotNull(method) { "method $name not found on ${target::class.java.name}" }
    method.isAccessible = true
    return method.invoke(target, *args)
  }

  @Test
  fun `table column get set and row index operators work`() {
    val row = Row("A")
    val mutableColumn = TableColumn<Row, String>(Row::name, "Name")

    assertThat(mutableColumn[row]).isEqualTo("A")
    mutableColumn[row] = "B"
    assertThat(row[mutableColumn]).isEqualTo("B")
  }

  @Test
  fun `table columns without setters fail on set`() {
    val row = Row("A")
    val readOnly = TableColumn<Row, Int>("size", getter = { it.name.length })

    assertThatThrownBy { readOnly[row] = 3 }
      .isInstanceOf(RuntimeException::class.java)
  }

  @Test
  fun `dynamic table column creates default cell label`() {
    val row = Row("Name")
    val column = DynamicTableColumn<Row, String>("name", getter = { it.name })

    val cell = column.createCellComponent(DmListItem(0, Model.of(row)), row)

    assertThat(cell).isInstanceOf(DmLabel::class.java)
  }

  @Test
  fun `table containers can be instantiated with models`() {
    val rows = Model.ofList(listOf(Row("A")))
    val cols = listOf(TableColumn<Row, String>(Row::name, "Name"))
    val groups = Model.ofList(listOf(DmGroupTableContainer.TableGroup("g", Row("A"))))

    val table = DmTableContainer("table", rows, cols)
    val groupTable = DmGroupTableContainer("groups", groups, cols)
    val dynamic = DynamicTableContainer("dynamic", rows, listOf(DynamicTableColumn("name", getter = { it.name })))

    assertThat(table.columns).hasSize(1)
    assertThat(groupTable.columns).hasSize(1)
    assertThat(dynamic.columns).hasSize(1)
  }

  @Test
  fun `custom border and list border contracts`() {
    val border = TestBorder("b")
    val listBorder = TestListBorder("list")

    assertThat(border.bodyContainer.id).contains("b")
    assertThatThrownBy { listBorder.add(DmLabel("x", "y")) }
      .isInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `autocomplete and multiselect borders keep constructor state`() {
    val auto = DmAutoCompleteDropdownPanel("auto", Model.of(""), "/api/options", "Label", "placeholder")
    val multi = MultiSelectBorder(
      id = "multi",
      label = "Users",
      listModel = Model.ofList(listOf("A")),
      optionsUrl = "/options",
      onSave = {},
      onDelete = {},
      populateItem = {}
    )

    assertThat(auto.optionsUrl).isEqualTo("/api/options")
    assertThat(multi.label).isEqualTo("Users")
  }

  @Test
  fun `property validated field factories attach validators`() {
    val text = createPropertyValidatedTextField<String>("t", Model.of("v"))
    val area = createPropertyValidatedTextArea("a", Model.of("v"))
    val email = createPropertyValidatedEmailField("e", Model.of("a@b.de"))
    val checkbox = createPropertyValidatedCheckBox("c", Model.of(true))
    val number = createPropertyValidatedNumberTextField<Int>("n", Model.of(5))
    val pattern = createPatternValidatedField<String>("p", Model.of("abc"), "^\\d+$", "digits only")

    invokeProtected(text, "onInitialize")
    invokeProtected(area, "onInitialize")
    invokeProtected(email, "onInitialize")
    invokeProtected(checkbox, "onInitialize")
    invokeProtected(number, "onInitialize")
    invokeProtected(pattern, "onInitialize")

    assertThat(text.validators).isNotEmpty
    assertThat(area.validators).isNotEmpty
    assertThat(email.validators).isNotEmpty
    assertThat(checkbox.validators).isNotEmpty
    assertThat(number.validators).isNotEmpty
    assertThat(pattern.validators).isNotEmpty
  }

  @Test
  fun `feedback panel css class maps levels`() {
    val panel = ExposedFeedbackPanel("fb")

    assertThat(panel.css(FeedbackMessage(panel, "ok", FeedbackMessage.SUCCESS))).isEqualTo("alert alert-success")
    assertThat(panel.css(FeedbackMessage(panel, "warn", FeedbackMessage.WARNING))).isEqualTo("alert alert-warning")
    assertThat(panel.css(FeedbackMessage(panel, "err", FeedbackMessage.ERROR))).isEqualTo("alert alert-danger")
  }

  @Test
  fun `validators are constructible`() {
    assertThat(DmStringValidator(1, 10)).isNotNull
    assertThat(MailValidator()).isNotNull
  }

  @Test
  fun `page annotation and params contract are available`() {
    val annotation = AnnotatedPage::class.java.getAnnotation(MountPage::class.java)
    val params = TestPageParams()

    assertThat(annotation.path).isEqualTo("/test")
    assertThat(annotation.deployment).isFalse()
    assertThat(params.pp).isNotNull
  }
}
