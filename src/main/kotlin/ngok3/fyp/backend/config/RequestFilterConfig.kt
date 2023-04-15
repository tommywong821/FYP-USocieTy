package ngok3.fyp.backend.config

import ngok3.fyp.backend.util.RequestLoggingFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun requestLoggingFilterRegistration(): FilterRegistrationBean<RequestLoggingFilter> {
        val registration = FilterRegistrationBean(RequestLoggingFilter())
        registration.order = Integer.MAX_VALUE - 1
        registration.addUrlPatterns("/*")
        return registration
    }
}