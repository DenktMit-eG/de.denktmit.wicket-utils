package de.denktmit.wicket.spring

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.apache.wicket.protocol.http.WicketFilter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import org.springframework.core.type.classreading.MetadataReader
import org.springframework.core.type.classreading.MetadataReaderFactory
import org.springframework.core.type.filter.TypeFilter

class SpringUtilitiesTest {

  private interface TestService

  @Test
  fun `spring bean lazily resolves and caches bean`() {
    val context = mockk<ApplicationContext>()
    val service = mockk<TestService>()
    every { context.getBean(TestService::class.java) } returns service
    SpringContextUtil.context = context

    val bean = bean<TestService>()

    assertThat(bean.isInitialized()).isFalse()
    assertThat(bean.value).isSameAs(service)
    assertThat(bean.isInitialized()).isTrue()
    verify(exactly = 1) { context.getBean(TestService::class.java) }
  }

  @Test
  fun `type filter not negates result`() {
    val includeFilter = TypeFilter { _: MetadataReader, _: MetadataReaderFactory -> true }
    val negated = includeFilter.not()

    val matches = negated.match(mockk(relaxed = true), mockk(relaxed = true))

    assertThat(matches).isFalse()
  }

  @Test
  fun `findClasses discovers mounted page in package`() {
    val classes = findClasses<Any>(this::class.java.packageName)

    assertThat(classes.map { it.name }).contains(TestMountedPage::class.java.name)
  }

  @Test
  fun `wicket filter wrapper delegates only matching paths`() {
    val wicketFilter = mockk<WicketFilter>(relaxed = true)
    every { wicketFilter.filterPath } returns "/app"
    val wrapper = WicketFilterWrapper(wicketFilter)
    val request = mockk<HttpServletRequest>()
    val response = mockk<ServletResponse>(relaxed = true)
    val chain = mockk<FilterChain>(relaxed = true)

    every { request.servletPath } returns "/app/page"
    wrapper.doFilter(request, response, chain)

    verify(exactly = 1) { wicketFilter.doFilter(request, response, chain) }
    verify(exactly = 0) { chain.doFilter(request, response) }

    every { request.servletPath } returns "/other"
    wrapper.doFilter(request, response, chain)

    verify(exactly = 1) { chain.doFilter(request, response) }
  }

  @Test
  fun `non http request is passed through`() {
    val wicketFilter = mockk<WicketFilter>(relaxed = true)
    val wrapper = WicketFilterWrapper(wicketFilter)

    wrapper.doFilter(mockk<ServletRequest>(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))

    verify(exactly = 0) { wicketFilter.doFilter(any(), any(), any()) }
  }
}
