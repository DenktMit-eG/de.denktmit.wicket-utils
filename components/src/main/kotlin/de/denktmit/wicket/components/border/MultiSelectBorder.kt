package de.denktmit.wicket.components.border

import de.denktmit.wicket.components.component.DmAjaxFallbackButton
import de.denktmit.wicket.components.component.DmAjaxLink
import de.denktmit.wicket.components.component.DmListItem
import de.denktmit.wicket.components.component.DmListView
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
  val populateItem: DmListItem<E>.(E) -> Unit,
) : Border(id) {
  var value: String? = null

  override fun addComponents() {
    enableAjax()

    addToBorder(bodyContainer)

    +DmListView("selected", listModel) { item ->
      populateItem(item)

      +DmAjaxLink<Any>("delete-item", click = {
        onDelete(item)
        it.add(this@MultiSelectBorder)
      })
    }

    addToBorder(
      DmAutoCompleteDropdownPanel(
        "autoCompletePanel",
        modelOf(this::value),
        optionsUrl = optionsUrl,
        labelName = label,
      ),
    )

    addToBorder(
      DmAjaxFallbackButton("submit-form", findParent(Form::class.java)) {
        onSubmit = { ajaxTarget ->
          onSave(this@MultiSelectBorder.value)
          this@MultiSelectBorder.value = null
          ajaxTarget?.add(this@MultiSelectBorder)
        }
      },
    )
  }
}
