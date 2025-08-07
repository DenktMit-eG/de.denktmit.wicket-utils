/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.denktmit.wicket.components.border

import de.denktmit.wicket.components.addCssClass
import de.denktmit.wicket.components.classNameAsCssClass
import de.denktmit.wicket.components.component.MyListItem
import de.denktmit.wicket.components.component.MyListView
import org.apache.wicket.*
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.*
import org.apache.wicket.markup.html.MarkupUtil
import org.apache.wicket.markup.html.WebMarkupContainer
import org.apache.wicket.markup.html.panel.BorderMarkupSourcingStrategy
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy
import org.apache.wicket.markup.parser.XmlTag
import org.apache.wicket.markup.resolver.IComponentResolver
import org.apache.wicket.model.IModel

abstract class ListBorder<T : Any, L : List<T>>
@JvmOverloads
constructor(
  id: String,
  val model: IModel<L>,
  @Transient
  val populateItem: MyListItem<T>.(T) -> Unit,
  @Transient
  private val init: ListBorder<T, L>.() -> Unit = {},
) : WebMarkupContainer(id, model),
  IComponentResolver,
  IQueueRegion {
  lateinit var bodyContainer: BorderListView<T, L>
    private set

  /** Convenience extension to make use of `+SomeComponent(...)` instead of `add(SomeComponent(...))` */
  operator fun Component.unaryPlus() {
    this@ListBorder.add(this)
  }

  operator fun Behavior.unaryPlus() {
    this@ListBorder.add(this)
  }

  override fun onInitialize() {
    bodyContainer =
      BorderListView("list", model) {
        +BorderListBodyContainer(id + "_" + BODY)
      }
    super.onInitialize()

    addCssClass("dm-border", "dm-id-$id", "dm-cmp-${classNameAsCssClass()}")
    addComponents()
    init()
  }

  @Transient
  var visible: (() -> Boolean)? = null

  override fun onConfigure() {
    super.onConfigure()
    dequeue()
    visible?.let { setVisible(it()) }
  }

  fun enableAjax() {
    outputMarkupId = true
    outputMarkupPlaceholderTag = true
  }

  /** Use [addToBorder] for direct children here */
  open fun addComponents() {
  }

  override fun add(vararg children: Component): ListBorder<T, L> =
    throw UnsupportedOperationException("Use the constructor parameter populateItem")

  override fun addOrReplace(vararg children: Component): ListBorder<T, L> =
    throw UnsupportedOperationException("Use the constructor parameter populateItem")

  override fun remove(component: Component): ListBorder<T, L> =
    throw UnsupportedOperationException("Use the constructor parameter populateItem")

  override fun remove(id: String): ListBorder<T, L> = throw UnsupportedOperationException("Use the constructor parameter populateItem")

  override fun removeAll(): ListBorder<T, L> = throw UnsupportedOperationException("Use the constructor parameter populateItem")

  override fun replace(replacement: Component): ListBorder<T, L> =
    throw UnsupportedOperationException("Use the constructor parameter populateItem")

  override fun queue(vararg components: Component): ListBorder<T, L> =
    throw UnsupportedOperationException("Use the constructor parameter populateItem")

  fun addToBorder(vararg children: Component?): ListBorder<T, L> {
    super.add(*children)
    return this
  }

  fun queueToBorder(vararg children: Component?): ListBorder<T, L> {
    super.queue(*children)
    return this
  }

  fun removeFromBorder(child: Component?): ListBorder<T, L> {
    super.remove(child)
    return this
  }

  fun replaceInBorder(component: Component?): ListBorder<T, L> {
    super.replace(component)
    return this
  }

  /**
   * {@inheritDoc}
   */
  override fun resolve(
    container: MarkupContainer,
    markupStream: MarkupStream,
    tag: ComponentTag,
  ): Component? {
    // make sure nested borders are resolved properly
    if (!bodyContainer.rendering) {
      // We are only interested in border body tags. The tag ID actually is irrelevant since
      // always preset with the same default
      if (TagUtils.isWicketBodyTag(tag)) {
        return bodyContainer
      }
    }
    return null
  }

  /**
   * {@inheritDoc}
   */
  override fun newMarkupSourcingStrategy(): IMarkupSourcingStrategy = BorderMarkupSourcingStrategy()

  /**
   * Search for the child markup in the file associated with the Border. The child markup must in
   * between the &lt;wicket:border&gt; tags.
   */
  override fun getMarkup(child: Component?): IMarkupFragment {
    // Border require an associated markup resource file
    val markup =
      associatedMarkup
        ?: throw MarkupException(
          "Unable to find associated markup file for Border: " +
            this.toString(),
        )

    // Find <wicket:border>
    var borderMarkup: IMarkupFragment? = null
    for (i in 0 until markup.size()) {
      val elem = markup[i]
      if (TagUtils.isWicketBorderTag(elem)) {
        borderMarkup = MarkupFragment(markup, i)
        break
      }
    }
    if (borderMarkup == null) {
      throw MarkupException(
        markup.markupResourceStream,
        "Unable to find <wicket:border> tag in associated markup file for Border: " +
          this.toString(),
      )
    }

    // If child == null, return the markup fragment starting with the <wicket:border> tag
    if (child == null) {
      return borderMarkup
    }

//    // Is child == BorderBody?
//    if (child === bodyContainer) {
//      // Get the <wicket:body> markup
//      return bodyContainer.markup
//    }

    // Find the markup for the child component
    val childMarkup = borderMarkup.find(child.id)
    return childMarkup
      ?: (markupSourcingStrategy as BorderMarkupSourcingStrategy).findMarkupInAssociatedFileHeader(
        this,
        child,
      )
  }

  class BorderListView<T : Any, L : List<T>>(
    id: String,
    model: IModel<L>,
    populateItem: MyListItem<T>.(T) -> Unit,
  ) : MyListView<T, L>(id, model, false, populateItem) {
    var rendering = false

    override fun onBeforeRender() {
      rendering = true
      super.onBeforeRender()
    }

    override fun onAfterRender() {
      super.onAfterRender()
      rendering = false
    }
  }

  /**
   * The container to be associated with the &lt;wicket:body&gt; tag
   */
  inner class BorderListBodyContainer(
    id: String?,
  ) : WebMarkupContainer(id),
    IQueueRegion {
    /** The markup  */
    @Transient
    private var markup: IMarkupFragment? = null

    override fun onComponentTag(tag: ComponentTag) {
      // Convert open-close to open-body-close
      if (tag.isOpenClose) {
        tag.type = XmlTag.TagType.OPEN
        tag.setModified(true)
      }
      super.onComponentTag(tag)
    }

    override fun onComponentTagBody(
      markupStream: MarkupStream,
      openTag: ComponentTag,
    ) {
      // skip the <wicket:body> body
      if (markupStream.previousTag.isOpen) {
        // Only RawMarkup is allowed within the preview region,
        // which gets stripped from output
        markupStream.skipRawMarkup()
      }

      // Get the <span wicket:id="myBorder"> markup and render that instead
      val markup = this@ListBorder.markup
      val stream = MarkupStream(markup)
      val tag = stream.tag
      stream.next()
      super.onComponentTagBody(stream, tag)
    }

    /**
     * Get the &lt;wicket:body&gt; markup from the body's parent container
     */
    override fun getMarkup(): IMarkupFragment {
      if (markup == null) {
        markup = findByName(parent.getMarkup(null), Border.BODY)
      }
      return markup!!
    }

    /**
     * Search for &lt;wicket:'name' ...&gt; on the same level, but ignoring other "transparent"
     * tags such as &lt;wicket:enclosure&gt; etc.
     *
     * @param markup
     * @param name
     * @return null, if not found
     */
    private fun findByName(
      markup: IMarkupFragment,
      name: String,
    ): IMarkupFragment? {
      val stream = MarkupStream(markup)

      // Skip any raw markup
      stream.skipUntil(ComponentTag::class.java)

      // Skip <wicket:border>
      stream.next()
      while (stream.skipUntil(ComponentTag::class.java)) {
        val tag = stream.tag
        if (tag.isOpen || tag.isOpenClose) {
          if (TagUtils.isWicketBodyTag(tag)) {
            return stream.markupFragment
          }
        }
        stream.next()
      }
      return null
    }

    /**
     * Get the child markup which must be in between the &lt;span wicktet:id="myBorder"&gt; tags
     */
    override fun getMarkup(child: Component): IMarkupFragment? {
      val markup = this@ListBorder.markup ?: return null
      return markup.find(child.id)
    }

    override fun newDequeueContext(): DequeueContext? {
      val border = findParent(ListBorder::class.java) as ListBorder<T, L>
      val fragment = border.markup ?: return null
      return DequeueContext(fragment, this, true)
    }

    override fun findComponentToDequeue(tag: ComponentTag): Component? {
      /*
       * the body container is allowed to search for queued components all
       * the way to the page even though it is an IQueueRegion so it can
       * find components queued below the border
       */
      var component = super.findComponentToDequeue(tag)
      if (component != null) {
        return component
      }
      var cursor = parent
      while (cursor != null) {
        component = cursor.findComponentToDequeue(tag)
        if (component != null) {
          return component
        }
        if (cursor is Border.BorderBodyContainer) {
          // optimization - find call above would've already recursed
          // to page
          break
        }
        cursor = cursor.parent
      }
      return null
    }
  }

  override fun canDequeueTag(tag: ComponentTag): DequeueTagAction? =
    if (canDequeueBody(tag)) {
      DequeueTagAction.DEQUEUE
    } else {
      super.canDequeueTag(tag)
    }

  override fun findComponentToDequeue(tag: ComponentTag): Component? {
    if (canDequeueBody(tag)) {
      // synch the tag id with the one of the body component
      tag.id = bodyContainer.id
    }
    return super.findComponentToDequeue(tag)
  }

  private fun canDequeueBody(tag: ComponentTag): Boolean = tag is WicketTag && tag.isBodyTag

  override fun addDequeuedComponent(
    component: Component,
    tag: ComponentTag,
  ) {
    // components queued in border get dequeued into the border not into the body container
    super.add(component)
  }

  /**
   * Returns the markup inside &lt;wicket:border&gt; tag.
   * If such tag is not found, all the markup is returned.
   *
   * @see IQueueRegion.getRegionMarkup
   */
  override fun getRegionMarkup(): IMarkupFragment {
//    val markup = super.getRegionMarkup()
//    if (markup == null) {
//      return markup
//    }
    val borderMarkup = MarkupUtil.findStartTag(markup, BORDER)
    return borderMarkup ?: markup
  }

  companion object {
    const val BODY = "body"
    const val BORDER = "border"
  }
}
