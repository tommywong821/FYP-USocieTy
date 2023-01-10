package ngok3.fyp.backend.authentication

import ngok3.fyp.backend.authentication.model.CasServiceResponse
import ngok3.fyp.backend.authentication.model.UserToken
import org.springframework.beans.factory.annotation.Autowired
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

    @PostMapping("/login")
    fun ITSCROPC(@RequestBody userinfo: Map<String, String>): String {
        return authService.ROPCLogin(userinfo)
    }

    @PostMapping("/mobileLogin")
    fun validateMobileLogin(@RequestBody userInfo: Map<String, String>): UserToken {
        return authService.validateMobileLogin(userInfo)
    }
}