package de.denktmit.wicket.components

import de.denktmit.wicket.components.resource.FileUploadResource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FileUploadResourceTest : WicketTestBase() {

  @Test
  fun `constructs with files key`() {
    val resource = FileUploadResource("files") {}

    assertThat(resource).isNotNull
  }
}
