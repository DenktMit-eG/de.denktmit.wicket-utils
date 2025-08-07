package de.denktmit.wicket.components.border

import de.denktmit.wicket.components.component.MyAjaxFallbackButton
import de.denktmit.wicket.components.component.MyAjaxLink
import de.denktmit.wicket.components.component.MyListItem
import de.denktmit.wicket.components.component.MyListView
import de.denktmit.wicket.model.modelOf
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
