package ngok3.fyp.backend.authentication

import ngok3.fyp.backend.authentication.model.AADProfile
import ngok3.fyp.backend.authentication.model.CasServiceResponse
import ngok3.fyp.backend.authentication.model.UserToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val authService: AuthService
) {
    @PostMapping("/serviceValidate")
    fun itscSSOServiceValidate(
        @RequestBody ticket: Map<String, String>,
        frontendResponse: HttpServletResponse
    ): CasServiceResponse {
        return this.authService.itscSSOServiceValidate(ticket, frontendResponse);
    }

    @PostMapping("/mockServiceValidate")
    fun mockItscSSOServiceValidate(
        @RequestBody ticket: Map<String, String>,
        frontendResponse: HttpServletResponse
    ): CasServiceResponse {
        return this.authService.mockItscSSOServiceValidate(ticket, frontendResponse);
    }

    @PostMapping("/mobileLogin")
    fun validateMobileLogin(@RequestBody aadProfile: AADProfile): UserToken {
        return authService.validateMobileLogin(aadProfile)
    }

    @PostMapping("/logout")
    fun logout(frontendResponse: HttpServletResponse): ResponseEntity<HashMap<String, String>> {
        return this.authService.logout(frontendResponse);
    }
}