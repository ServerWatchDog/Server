package i.server.handler.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingRequestWrapper

@Component
class RequestJsonFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest) {
            chain.doFilter(ContentCachingRequestWrapper(request), response)
        } else {
            chain.doFilter(request, response)
        }
    }
}
