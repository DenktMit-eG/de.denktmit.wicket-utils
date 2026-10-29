package de.denktmit.wicket.components.visitor

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
      // the second type parameter is named like the type it always is; the cast keeps the published signature
      @Suppress("UNCHECKED_CAST")
      visit?.stop(`object`.pageRelativePath as String)
    }
  }
}
