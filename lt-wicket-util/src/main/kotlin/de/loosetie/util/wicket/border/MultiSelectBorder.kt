package de.loosetie.util.wicket.border

import de.loosetie.util.wicket.component.MyAjaxFallbackButton
import de.loosetie.util.wicket.component.MyAjaxLink
import de.loosetie.util.wicket.component.MyListItem
import de.loosetie.util.wicket.component.MyListView
import de.loosetie.util.wicket.modelOf
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.IModel

/**
 * Use This markup:
 *
 *    <ul class="dm-selected list-group">
 *      <li wicket:id="selected" class="dm-item list-group-item">
 *        ...
 *        <a class="btn btn-danger float-right" wicket:id="delete-item">
 *          <i class="fas fa-trash-alt"></i>
 *        </a>
 *      </li>
 *    </ul>
 */
class MultiSelectBorder<E : Any>(
  id: String,
  val label: String,
  val listModel: IModel<List<E>>,
  val optionsUrl: String,
  @Transient
  val onSave: (String?) -> Unit,
  @Transient
  val onDelete: (E) -> Unit,
  @Transient
  val populateItem: MyListItem<E>.(E) -> Unit,
) : Border(id) {
  var value: String? = null

  override fun addComponents() {
    enableAjax()

    addToBorder(bodyContainer)

    +MyListView("selected", listModel) { item ->
      populateItem(item)

      +MyAjaxLink<Any>("delete-item", click = {
        onDelete(item)
        it.add(this@MultiSelectBorder)
      })
    }

    addToBorder(
      MyAutoCompleteDropdownPanel(
        "autoCompletePanel",
        modelOf(this::value),
        optionsUrl = optionsUrl,
        labelName = label,
      ),
    )

    addToBorder(
      MyAjaxFallbackButton("submit-form", findParent(Form::class.java)) {
        onSubmit = { ajaxTarget ->
          onSave(this@MultiSelectBorder.value)
          this@MultiSelectBorder.value = null
          ajaxTarget?.add(this@MultiSelectBorder)
        }
      },
    )
  }
}
