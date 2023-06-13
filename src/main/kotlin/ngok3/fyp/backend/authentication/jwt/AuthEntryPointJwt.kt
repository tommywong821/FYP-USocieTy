package ngok3.fyp.backend.authentication.jwt

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        print("Unauthorized error: ${authException?.message}")
        val cookie: ResponseCookie =
            ResponseCookie.from("token", "").httpOnly(true).secure(true).path("/")
                .maxAge(0).sameSite("None").build()
        response?.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
    }
}