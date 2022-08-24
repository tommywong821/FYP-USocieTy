package ngok3.fyp.backend.authentication

import ngok3.fyp.backend.authentication.model.CasServiceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/mockServiceValidate")
    fun mockItscSSOServiceValidate(
        @RequestParam("ticket") ticket: String,
        frontendResponse: HttpServletResponse
    ) {
        return this.authService.mockItscSSOServiceValidate(ticket, frontendResponse);
    }

    @PostMapping("/login")
    fun ITSCROPC(@RequestBody userinfo: Map<String, String>): String {
        return authService.ROPCLogin(userinfo)
    }
}