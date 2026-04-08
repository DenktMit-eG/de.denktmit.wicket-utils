package de.denktmit.wicket.spring

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterConfig
import org.apache.wicket.protocol.http.WicketFilter
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext

class SpringBranchCoverageTest {

  private interface Service

  @Test
  fun `wicket filter wrapper delegates init and destroy`() {
    val wicketFilter = mockk<WicketFilter>(relaxed = true)
    val wrapper = WicketFilterWrapper(wicketFilter)
    val config = mockk<FilterConfig>()

    wrapper.init(config)
    wrapper.destroy()

    verify(exactly = 1) { wicketFilter.init(config) }
    verify(exactly = 1) { wicketFilter.destroy() }
  }

  @Test
  fun `spring bean getter setter and top level bean helper are reachable`() {
    val context = mockk<ApplicationContext>()
    val service = mockk<Service>()
    every { context.getBean(Service::class.java) } returns service
    SpringContextUtil.context = context

    val reflectedBean = SpringBean(Service::class.java)

    reflectedBean.bean = service

    assertThat(reflectedBean.type).isEqualTo(Service::class.java)
    assertThat(reflectedBean.name).isNull()
    assertThat(reflectedBean.value).isSameAs(service)

    assertThatThrownBy {
      Class.forName("de.denktmit.wicket.spring.SpringBeanKt")
        .getDeclaredMethod("bean", String::class.java)
        .invoke(null, null)
    }.hasRootCauseInstanceOf(UnsupportedOperationException::class.java)
  }

  @Test
  fun `reified bean helper returns spring bean wrapper`() {
    val wrapped = bean<Service>()

    assertThat(wrapped.type).isEqualTo(Service::class.java)
    assertThat(wrapped.name).isNull()
  }

  @Test
  fun `findClasses default helper method is reachable`() {
    assertThatThrownBy {
      Class.forName("de.denktmit.wicket.spring.ClasspathUtilKt")
        .getDeclaredMethod(
          "findClasses\$default",
          Array<String>::class.java,
          List::class.java,
          Int::class.javaPrimitiveType,
          Any::class.java,
        )
        .invoke(null, arrayOf(this::class.java.packageName), emptyList<Class<out Annotation>>(), 2, null)
    }.hasRootCauseInstanceOf(UnsupportedOperationException::class.java)
  }
}
