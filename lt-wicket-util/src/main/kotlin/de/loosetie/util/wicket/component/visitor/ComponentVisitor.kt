package de.loosetie.util.wicket.component.visitor

import org.apache.wicket.Component
import org.apache.wicket.util.visit.IVisit
import org.apache.wicket.util.visit.IVisitor

class ComponentVisitor(
  val idComponent: String,
) : IVisitor<Component, Component> {
  override fun component(
    `object`: Component,
    visit: IVisit<Component>?,
  ) {
    if (`object`.id == idComponent) {
      visit?.stop(`object`)
    }
  }
}
