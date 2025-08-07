package de.denktmit.wicket.components.component

import de.denktmit.wicket.model.modelOf
import org.apache.wicket.Component
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import kotlin.reflect.KProperty0

open class DmListView<T : Any, L : List<T>?>(
  id: String,
  model: IModel<L>,
  var inline: Boolean = false,
  @Transient
  val populateItem: DmListItem<T>.(T) -> Unit,
) : ListView<T>(id, model) {
  constructor(
    field: KProperty0<L>,
    id: String = field.name,
    model: IModel<L> = modelOf(field),
    inline: Boolean = false,
    populateItem: DmListItem<T>.(T) -> Unit,
  ) : this(id, model, inline, populateItem)

  override fun populateItem(item: ListItem<T>) {
    item as DmListItem<T>
    item.populateItem(item.modelObject)
  }

  override fun newItem(
    index: Int,
    itemModel: IModel<T>,
  ): ListItem<T> = DmListItem(index, itemModel)

  override fun onComponentTag(tag: ComponentTag) {
    if (inline) {
      tag.name = "span"
    } else {
      super.onComponentTag(tag)
    }
  }

  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }
}

class DmListItem<T : Any>(
  index: Int,
  model: IModel<T>,
) : ListItem<T>(index, model) {
  operator fun Component.unaryPlus() {
    this@DmListItem.add(this)
  }

  operator fun component1(): T = modelObject
}
