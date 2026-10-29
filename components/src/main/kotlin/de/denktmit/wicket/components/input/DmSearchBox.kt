package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.display.DmIcon
import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.form.Button
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A text with a search action, a clear control and recent values (requirement E.55): a native `input type="search"`
 * (which clears itself in most browsers), a submit button that runs [onSearch], a clear button that submits an empty
 * value, and [recent] values offered through a `datalist`.
 */
open class DmSearchBox
@JvmOverloads
constructor(
  id: String,
  model: IModel<String?>,
  /** Values offered while typing, most recent first. */
  val recent: IModel<out List<String>> = Model.ofList(emptyList()),
  val placeholder: IModel<String> = Model.of("Search"),
  init: DmInputPanel<String?>.() -> Unit = {},
) : DmInputPanel<String?>(id, model, init) {

  /** Called with the query when the search button submits the form. */
  @Transient
  var onSearch: ((String?) -> Unit)? = null

  /** The native search input. */
  val input: TextField<String> = object : TextField<String>("query", Model(model.`object`)) {
    override fun onComponentTag(tag: ComponentTag) {
      super.onComponentTag(tag)
      tag.put("type", "search")
      tag.put("placeholder", this@DmSearchBox.placeholder.`object`)
      if (recent.`object`.isNotEmpty()) tag.put("list", this@DmSearchBox.markupId + "-recent")
    }

    override fun getInputTypes(): Array<String> = arrayOf("search")
  }

  private var cleared = false

  override fun addComponents() {
    outputMarkupId = true
    +input
    +object : Button("search") {
      override fun onSubmit() {
        onSearch?.invoke(this@DmSearchBox.modelObject)
      }
    }.apply { setDefaultFormProcessing(true); add(DmIcon("icon", "search")) }
    +object : Button("clear") {
      override fun onSubmit() {
        cleared = true
        this@DmSearchBox.modelObject = null
        this@DmSearchBox.input.modelObject = null
        onSearch?.invoke(null)
      }
    }.apply { setDefaultFormProcessing(false); add(DmIcon("icon", "close")) }
    +WebMarkupContainer("recent").apply {
      setMarkupId(this@DmSearchBox.markupId + "-recent")
      add(object : ListView<String>("option", recent) {
        override fun populateItem(item: ListItem<String>) {
          item.add(org.apache.wicket.AttributeModifier.replace("value", item.modelObject))
        }
      })
      add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: org.apache.wicket.Component) {
          component.isVisible = recent.`object`.isNotEmpty()
        }
      })
    }
  }

  override fun onBeforeRender() {
    input.modelObject = modelObject
    super.onBeforeRender()
  }

  override fun labelTarget(): FormComponent<*> = input

  override fun convertInput() {
    convertedInput = input.convertedInput?.trim()?.ifEmpty { null }
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    tag.put("role", "search")
    tag.put("data-dm-search-box", "")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-search-box")
}
