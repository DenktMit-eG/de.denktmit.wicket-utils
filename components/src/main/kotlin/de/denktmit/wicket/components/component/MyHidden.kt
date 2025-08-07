package de.denktmit.wicket.components.component

open class MyHidden(
  id: String,
) : MyPanel(id) {
  override fun onConfigure() {
    super.onConfigure()
    isVisible = false
  }
}
