package ngok3.fyp.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebServerConfiguration {
    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    @Bean
    fun addCorsConfig(): WebMvcConfigurer {
        val allowedOrigins = corsOriginPatterns.split(",").toTypedArray()
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOriginPatterns(*allowedOrigins)
                    .allowCredentials(true)
            }
        }
    }
}