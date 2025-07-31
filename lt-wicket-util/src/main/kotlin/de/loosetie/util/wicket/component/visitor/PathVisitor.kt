package de.loosetie.util.wicket.component.visitor

import org.apache.wicket.Component
import org.apache.wicket.util.visit.IVisit
import org.apache.wicket.util.visit.IVisitor

class PathVisitor<R : Component, String>(
  val idComponent: String,
) : IVisitor<R, String> {
  override fun component(
    `object`: R,
    visit: IVisit<String>?,
  ) {
    if (`object`.id == idComponent) {
      visit?.stop(`object`.pageRelativePath as String)
    }
  }
}
