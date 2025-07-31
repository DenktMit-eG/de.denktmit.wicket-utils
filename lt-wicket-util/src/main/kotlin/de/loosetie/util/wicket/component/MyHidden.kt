package de.loosetie.util.wicket.component

open class MyHidden(
  id: String,
) : MyPanel(id) {
  override fun onConfigure() {
    super.onConfigure()
    isVisible = false
  }
}
