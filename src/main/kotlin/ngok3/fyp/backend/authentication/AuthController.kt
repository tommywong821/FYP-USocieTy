package ngok3.fyp.backend.authentication

import ngok3.fyp.backend.authentication.model.CasServiceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val authService: AuthService
) {
    @GetMapping("/serviceValidate")
    fun itscSSOServiceValidate(
        @RequestParam("ticket") ticket: String,
        frontendResponse: HttpServletResponse
    ): CasServiceResponse {
        return this.authService.itscSSOServiceValidate(ticket, frontendResponse);
    }
}