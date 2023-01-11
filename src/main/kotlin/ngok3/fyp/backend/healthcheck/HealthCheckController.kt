package ngok3.fyp.backend.healthcheck

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthCheckController {
    @GetMapping("/cookie")
    fun healthCheck(@CookieValue(name = "token", required = false) token: String): String {
        print(token)
        return "cookie OK"
    }

    @GetMapping("/testNoRole")
    @PreAuthorize("hasRole('test')")
    fun healthCheck(): String {
        return "OK"
    }

    @GetMapping("/testRole")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    fun healthCheck2(): String {
        return "OK"
    }
}