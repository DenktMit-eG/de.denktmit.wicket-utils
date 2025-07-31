package de.loosetie.util.wicket

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import org.apache.wicket.protocol.http.WicketFilter

class WicketFilterWrapper(
  val wicketFilter: WicketFilter,
) : Filter {
  override fun init(filterConfig: FilterConfig?) {
    wicketFilter.init(filterConfig)
  }

  override fun destroy() {
    wicketFilter.destroy()
  }

  override fun doFilter(
    request: ServletRequest,
    response: ServletResponse,
    chain: FilterChain,
  ) {
    if (request is HttpServletRequest) {
      val relativePath = request.servletPath
      if (relativePath.startsWith("/${wicketPath()}")) {
        return wicketFilter.doFilter(request, response, chain)
      }
    }
    chain.doFilter(request, response)
  }

  private fun wicketPath() = wicketFilter.filterPath.removeSuffix("/")
}
