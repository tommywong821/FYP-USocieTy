package ngok3.fyp.backend.config

import ngok3.fyp.backend.authentication.jwt.AuthEntryPointJwt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
class WebSecurityConfig(
//    private val jwtAuthenticationFilter: JWTAuthenticationFilter,
    @Autowired private val unauthorizedHandler: AuthEntryPointJwt
) {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http {
            //allow postman and frontend access
            cors {
                disable()
            }
            csrf {
                disable()
            }
            exceptionHandling {
                authenticationEntryPoint = unauthorizedHandler
            }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeRequests {
                authorize("/swagger-ui/**", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                //web login endpoint
                authorize("/auth/serviceValidate", permitAll)
                authorize("/auth/mockServiceValidate", permitAll)
                //mobile login endpoint
                authorize("/auth/mobileLogin", permitAll)
                authorize("/health", permitAll)
                authorize(anyRequest, authenticated)
            }
            //filter for each request
//            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
        }
        return http.build()
    }
}
