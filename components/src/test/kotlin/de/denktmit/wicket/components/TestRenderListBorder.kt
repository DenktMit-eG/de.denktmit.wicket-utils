package de.denktmit.wicket.components

import de.denktmit.wicket.components.border.ListBorder
import de.denktmit.wicket.components.component.DmLabel
import de.denktmit.wicket.components.component.DmListItem
import org.apache.wicket.model.IModel

class TestRenderListBorder(
  id: String,
  model: IModel<List<String>>,
) : ListBorder<String, List<String>>(id, model, populateItem = { value ->
  add(DmLabel("item", value))
}) {
  override fun addComponents() {
    addToBorder(DmLabel("title", "list-title"))
  }
}
