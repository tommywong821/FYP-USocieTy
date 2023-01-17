package ngok3.fyp.backend.authentication

import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.authentication.model.AADProfile
import ngok3.fyp.backend.authentication.model.UserToken
import ngok3.fyp.backend.operation.student.StudentDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(
    @Autowired val authService: AuthService
) {
    @Operation(summary = "Verify cas ticket pass from admin page and sign cookie to admin page")
    @PostMapping("/serviceValidate")
    fun itscSSOServiceValidate(
        @RequestBody ticket: Map<String, String>,
        frontendResponse: HttpServletResponse
    ): StudentDto {
        return this.authService.itscSSOServiceValidate(ticket, frontendResponse);
    }

    @Operation(summary = "Testing cookie")
    @PostMapping("/mockServiceValidate")
    fun mockItscSSOServiceValidate(
        @RequestBody ticket: Map<String, String>,
        frontendResponse: HttpServletResponse
    ): StudentDto {
        return this.authService.mockItscSSOServiceValidate(ticket, frontendResponse);
    }

    @Operation(summary = "Handle mobile login and sign cookie to flutter secure storage")
    @PostMapping("/mobileLogin")
    fun validateMobileLogin(@RequestBody aadProfile: AADProfile): UserToken {
        return authService.validateMobileLogin(aadProfile)
    }

    @Operation(summary = "Clear cookie of admin page")
    @PostMapping("/logout")
    fun logout(frontendResponse: HttpServletResponse): ResponseEntity<HashMap<String, String>> {
        return this.authService.logout(frontendResponse);
    }
}