package de.denktmit.wicket.spring

import io.mockk.every
import io.mockk.mockk
import org.apache.wicket.Page
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.util.tester.WicketTester
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext

class SpringCoverageTest {

  private interface NamedService

  private class HomePage : WebPage()

  private class MountingTestApp : WebApplication() {
    override fun getHomePage(): Class<out Page> = HomePage::class.java
  }

  @Test
  fun `spring bean resolves named bean and throws when missing`() {
    val context = mockk<ApplicationContext>()
    val named = mockk<NamedService>()
    every { context.getBean("named", NamedService::class.java) } returns named
    every { context.getBean(NamedService::class.java) } throws IllegalStateException("missing")
    SpringContextUtil.context = context

    val bean = bean<NamedService>("named")

    assertThat(bean.value).isSameAs(named)
    assertThatThrownBy { SpringBean(NamedService::class.java).value }
      .isInstanceOf(IllegalStateException::class.java)
  }

  @Test
  fun `setApplicationContext updates static context`() {
    val util = SpringContextUtil()
    val context = mockk<ApplicationContext>()

    util.setApplicationContext(context)

    assertThat(SpringContextUtil.context).isSameAs(context)
  }

  @Test
  fun `mountAnnotatedPages executes without failure`() {
    val app = MountingTestApp()
    val tester = WicketTester(app)

    try {
      app.mountAnnotatedPages()
      assertThat(app).isNotNull
    } finally {
      tester.destroy()
    }
  }
}
