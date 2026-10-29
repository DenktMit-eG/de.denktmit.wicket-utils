package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.form.DropDownChoice
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.markup.html.form.IChoiceRenderer
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.repeater.RepeatingView
import org.apache.wicket.model.IModel
import org.apache.wicket.model.LoadableDetachableModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference

/**
 * A value chosen through levels (requirement E.46), country, region, city: one native `select` per level, the
 * options of a level being the [children] of the value chosen above it. A change refreshes the lower levels by
 * Ajax; without script the form submit does the same, so the user picks a level, submits, and goes on. The model
 * is the chosen path, root first.
 */
open class DmCascader<T : java.io.Serializable>
@JvmOverloads
constructor(
  id: String,
  model: IModel<List<T>>,
  /** The number of levels. */
  val levels: Int,
  @Transient
  private val children: (T?) -> List<T>,
  renderer: IChoiceRenderer<in T>,
  /** The labels of the levels, used as the empty choice and the accessible name. */
  val labels: List<String> = (1..levels).map { "Level $it" },
  init: DmInputPanel<List<T>>.() -> Unit = {},
) : DmInputPanel<List<T>>(id, model, init) {

  private val current: MutableList<T?> = MutableList(levels) { model.`object`.getOrNull(it) }
  private val selects = ArrayList<DropDownChoice<T>>()
  private val renderer = renderer

  override fun addComponents() {
    outputMarkupId = true
    val list = RepeatingView("levels")
    +list
    for (level in 0 until levels) {
      val select = object : DropDownChoice<T>(
        list.newChildId(),
        object : IModel<T?> {
          override fun getObject(): T? = current[level]

          override fun setObject(value: T?) {
            current[level] = value
            for (deeper in level + 1 until levels) current[deeper] = null
          }
        },
        object : LoadableDetachableModel<List<T>>() {
          override fun load(): List<T> = if (level == 0) children(null) else current[level - 1]?.let(children) ?: emptyList()
        },
        renderer,
      ) {
        override fun onComponentTag(tag: ComponentTag) {
          super.onComponentTag(tag)
          tag.put("aria-label", labels[level])
        }

        override fun getNullValidDisplayValue(): String = labels[level]
      }
      select.isNullValid = true
      select.add(object : OnChangeAjaxBehavior() {
        override fun onUpdate(target: AjaxRequestTarget) {
          target.add(this@DmCascader)
        }
      })
      select.add(object : org.apache.wicket.behavior.Behavior() {
        override fun onConfigure(component: org.apache.wicket.Component) {
          component.isVisible = level == 0 || current[level - 1] != null
        }
      })
      selects.add(select)
      list.add(select)
    }
  }

  /** The select of [level], root first. */
  fun select(level: Int): DropDownChoice<T> = selects[level]

  override fun labelTarget(): FormComponent<*> = selects.first()

  override fun convertInput() {
    val path = ArrayList<T>()
    for (select in selects) {
      val value = if (select.isVisibleInHierarchy) select.convertedInput else null
      if (value == null) break
      path.add(value)
    }
    convertedInput = path
  }

  override fun updateModel() {
    super.updateModel()
    for (level in 0 until levels) current[level] = modelObject.getOrNull(level)
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-cascader")
}
