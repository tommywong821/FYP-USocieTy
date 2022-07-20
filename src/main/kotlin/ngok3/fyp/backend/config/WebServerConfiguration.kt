package ngok3.fyp.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebServerConfiguration {
    @Bean
    fun addCorsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOriginPatterns("http://localhost:4200", "https://ngok3fyp-frontend.herokuapp.com/")
                    .allowCredentials(true)
            }
        }
    }
}