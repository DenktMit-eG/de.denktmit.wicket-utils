package de.denktmit.wicket.spring

import org.apache.wicket.protocol.http.WicketFilter
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest


class WicketFilterWrapper(
    val wicketFilter: WicketFilter
) : Filter {
    override fun init(filterConfig: FilterConfig?) {
        wicketFilter.init(filterConfig)
    }

    override fun destroy() {
        wicketFilter.destroy()
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest) {
            val relativePath = request.servletPath
            if (relativePath.startsWith("/${wicketPath()}"))
                return wicketFilter.doFilter(request, response, chain)
        }
        chain.doFilter(request, response)
    }

    private fun wicketPath() =
        wicketFilter.filterPath.removePrefix("/").removeSuffix("/")
}