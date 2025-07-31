package de.loosetie.util.wicket.component

import de.loosetie.util.wicket.set
import org.apache.wicket.AttributeModifier
import org.apache.wicket.Component
import org.apache.wicket.Page
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.model.IModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import kotlin.reflect.KProperty

open class MyPageLink<C : Page>(
  id: String,
  pageClass: Class<C>,
  parameters: PageParameters? = null,
  val bodyModel: IModel<String>? = null,
  @Transient
  private val init: MyPageLink<C>.() -> Unit = {},
) : BookmarkablePageLink<Any>(id, pageClass, parameters) {
  constructor(
    id: String,
    pageRef: PageRef<C>,
    bodyModel: IModel<String>? = null,
    init: MyPageLink<C>.() -> Unit = {},
  ) : this(id, pageRef.pageClass, pageRef.parameters, bodyModel, init)

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@MyPageLink.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@MyPageLink.add(this)
  }

  override fun onInitialize() {
    super.onInitialize()
    addCssClass("lt-link", "lt-page-link", "lt-id-$id")
    body = bodyModel
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    visible?.let { setVisible(it()) }
    if (isVisible) {
      for ((property, value) in paramModels) {
        pageParameters.set(property.name, value.`object`)
      }
    }
  }

  val paramModels = mutableMapOf<KProperty<Any?>, IModel<out Any?>>()

  fun <T : String?> param(
    property: KProperty<T>,
    value: T,
  ) {
    pageParameters.set(property, value)
  }

  fun <T : String?> param(
    property: KProperty<T>,
    model: IModel<T>,
  ) {
    paramModels[property] = model
  }

  var tooltip: String
    get() = markupAttributes["title"] as? String ?: ""
    set(value) {
      add(AttributeModifier("title", value))
    }
}

data class PageRef<P : Page>(
  val pageClass: Class<P>,
  val parameters: PageParameters = PageParameters(),
) {
  fun <T> param(
    property: KProperty<T>,
    value: T,
  ): PageRef<P> {
    parameters.set(property.name, value)
    return this
  }
}
