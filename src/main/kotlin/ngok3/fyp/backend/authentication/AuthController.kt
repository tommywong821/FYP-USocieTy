package ngok3.fyp.backend.authentication

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val authService: AuthService
) {
    @GetMapping
    @RequestMapping("/login")
    fun itscSSOLogin(): String {
        return this.authService.itscSSOLogin();
    }
}