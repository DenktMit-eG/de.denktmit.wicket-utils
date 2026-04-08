package de.denktmit.wicket.components

import de.denktmit.wicket.components.form.DmFileUpload
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DmFileUploadTest : WicketTestBase() {

  @Test
  fun `constructs with id`() {
    val upload = DmFileUpload("fu")

    assertThat(upload.id).isEqualTo("fu")
  }

  @Test
  fun `onInitialize adds CSS classes and calls init`() {
    var initCalled = false
    val upload = DmFileUpload("fu") { initCalled = true }
    invokeDeclared(upload, "onInitialize")

    assertThat(initCalled).isTrue()
    assertThat(upload.behaviors).isNotEmpty
  }
}
