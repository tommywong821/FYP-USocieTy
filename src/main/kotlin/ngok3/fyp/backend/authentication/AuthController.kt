package ngok3.fyp.backend.authentication

import ngok3.fyp.backend.authentication.model.ServiceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val authService: AuthService
) {
    @GetMapping
    @RequestMapping("/serviceValidate")
    fun itscSSOServiceValidate(@RequestParam("ticket") ticket: String): ServiceResponse {
        return this.authService.itscSSOServiceValidate(ticket);
    }
}