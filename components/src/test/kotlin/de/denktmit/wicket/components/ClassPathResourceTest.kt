package de.denktmit.wicket.components

import de.denktmit.wicket.components.resource.ClassPathResource
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.wicket.request.http.WebResponse
import org.apache.wicket.request.resource.IResource
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class ClassPathResourceTest : WicketTestBase() {

  @Test
  fun `stores classpath path`() {
    val resource = ClassPathResource("missing.file")

    assertThat(resource.path).isEqualTo("missing.file")
  }

  @Test
  fun `respond writes existing resource bytes`() {
    val attrs = mockk<IResource.Attributes>()
    val response = mockk<WebResponse>(relaxed = true)
    val output = ByteArrayOutputStream()
    every { attrs.response } returns response
    every { response.outputStream } returns output
    val resource = ClassPathResource("de/denktmit/wicket/components/ClassPathResourceTest.class")

    resource.respond(attrs)

    verify { response.setContentType(any()) }
    assertThat(output.size()).isGreaterThan(0)
  }

  @Test
  fun `respond throws for missing resource`() {
    val attrs = mockk<IResource.Attributes>()
    every { attrs.response } returns mockk<WebResponse>(relaxed = true)

    assertThatThrownBy { ClassPathResource("missing.file").respond(attrs) }
      .isInstanceOf(RuntimeException::class.java)
  }
}
