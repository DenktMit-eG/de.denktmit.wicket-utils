package de.loosetie.util.wicket

import org.apache.wicket.Component
import org.apache.wicket.MarkupContainer

fun Any.classNameAsCssClass(): String =
  javaClass.simpleName
    .replace(Regex("(\\B[A-Z])")) {
      "-" + it.value
    }.lowercase()

fun <TC : Component, TM : MarkupContainer> TM.q(component: TC): TC = component.also { queue(it) }
