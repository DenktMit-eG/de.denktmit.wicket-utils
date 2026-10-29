package de.denktmit.wicket.components.input

import de.denktmit.wicket.components.resource.DmResources
import de.denktmit.wicket.components.widget.DmInputPanel
import de.denktmit.wicket.components.widget.DmWidgetBorder
import org.apache.wicket.AttributeModifier
import org.apache.wicket.Component
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter
import org.apache.wicket.feedback.FeedbackCollector
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.FormComponent
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.request.resource.ResourceReference
import org.apache.wicket.util.visit.IVisit
import org.apache.wicket.util.visit.IVisitor

/**
 * The frame of one input in a form (requirements E.57, E.58): a label bound to the input, an optional help text,
 * a required mark, and the input's own error messages placed next to it and announced; the summary stays in the
 * page's feedback panel. The input is the first form component in the author's markup inside the border; a
 * composite input ([DmInputPanel]) names the inner input the label points at.
 */
open class DmFormField
@JvmOverloads
constructor(
  id: String,
  label: IModel<String>,
  private val help: IModel<String>? = null,
  init: DmWidgetBorder.() -> Unit = {},
) : DmWidgetBorder(id, label, init) {

  @JvmOverloads
  constructor(id: String, label: String, help: String? = null, init: DmWidgetBorder.() -> Unit = {}) :
    this(id, Model.of(label), help?.let { Model.of(it) }, init)

  private lateinit var labelTag: Label
  private lateinit var errorTag: Label

  /** The form component this field frames, found in the body on first render. */
  fun input(): FormComponent<*>? = bodyContainer.visitChildren(FormComponent::class.java, IVisitor<FormComponent<*>, FormComponent<*>> { component, visit: IVisit<FormComponent<*>> ->
    if (component.parent === bodyContainer || component.findParent(FormComponent::class.java) == null) visit.stop(component)
  })

  /** The element the `label` points at: the inner input of a composite, else the input itself. */
  private fun labelTarget(): Component? = input()?.let { if (it is DmInputPanel<*>) it.labelTarget() ?: it else it }

  private fun firstError(): String? = input()?.let { FeedbackCollector(page).collect(ComponentFeedbackMessageFilter(it)).firstOrNull()?.message?.toString() }

  override fun addComponents() {
    outputMarkupId = true
    @Suppress("UNCHECKED_CAST")
    labelTag = Label("label", defaultModel as IModel<String>)
    errorTag = Label("error", object : IModel<String> { override fun getObject(): String = firstError() ?: "" })
    addToBorder(labelTag)
    addToBorder(Label("help", help ?: Model.of("")).apply { isVisible = help != null; setOutputMarkupId(true); setMarkupId(this@DmFormField.markupId + "-help") })
    addToBorder(errorTag.apply { setOutputMarkupId(true); setMarkupId(this@DmFormField.markupId + "-error") })
  }

  override fun onBeforeRender() {
    val input = input()
    val target = labelTarget()
    if (target != null) {
      target.outputMarkupId = true
      labelTag.add(AttributeModifier.replace("for", target.markupId))
    }
    if (input != null) {
      @Suppress("UNCHECKED_CAST")
      input.label = defaultModel as IModel<String>
      val error = firstError()
      errorTag.isVisible = error != null
      val described = listOfNotNull(if (help != null) "${markupId}-help" else null, if (error != null) "${markupId}-error" else null)
      (target ?: input).add(AttributeModifier.replace("aria-describedby", described.joinToString(" ").ifEmpty { null }))
      (target ?: input).add(AttributeModifier.replace("aria-invalid", if (error != null) "true" else null))
    } else {
      errorTag.isVisible = false
    }
    super.onBeforeRender()
  }

  override fun onComponentTag(tag: ComponentTag) {
    super.onComponentTag(tag)
    if (input()?.isRequired == true) tag.append("class", "dm-form-field-required", " ")
    if (firstError() != null) tag.append("class", "dm-form-field-invalid", " ")
  }

  override fun styleSheet(): ResourceReference = DmResources.css("dm-form-field")
}
