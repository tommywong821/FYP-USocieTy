package ngok3.fyp.backend.util

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class RequestLoggingFilter : Filter {

    private val logger = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val requestUrl = httpRequest.requestURL.toString()
        val requestMethod = httpRequest.method
        val requestHeaders = httpRequest.headerNames.toList().joinToString(", ") {
            "$it=${httpRequest.getHeader(it)}"
        }
        val requestBody = httpRequest.reader.readText()

        logger.info("Incoming request: $requestMethod $requestUrl, headers: $requestHeaders, body: $requestBody")

        chain.doFilter(request, response)
    }
}