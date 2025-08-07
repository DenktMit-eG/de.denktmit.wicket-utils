package de.denktmit.wicket.components.component

open class DmHidden(
  id: String,
) : DmPanel(id) {
  override fun onConfigure() {
    super.onConfigure()
    isVisible = false
  }
}
