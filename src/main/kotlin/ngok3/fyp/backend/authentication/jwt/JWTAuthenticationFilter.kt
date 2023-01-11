package ngok3.fyp.backend.authentication.jwt

import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTAuthenticationFilter(
    @Autowired val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            logger.info("executing request internal filter")
            //allow angular preflight
            if (HttpMethod.OPTIONS.toString() != request.method) {
                //handle normal restful
                val cookies: Array<Cookie> = request.cookies
                for (cookie in cookies) {
                    if ("token" == cookie.name) {
                        val jwtToken: String = cookie.value
                        //decode jwt token
                        val claims: Claims = jwtUtil.verifyToken(jwtToken)
                        //extract role from cookies
                        val userRoleList: List<String> = claims["role"].toString().split(",")
                        //form authority from role list
                        val authorities: List<GrantedAuthority> =
                            userRoleList.map { role -> SimpleGrantedAuthority(role) }
                        val authentication: UsernamePasswordAuthenticationToken =
                            UsernamePasswordAuthenticationToken(claims, null, authorities)

                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }
            filterChain.doFilter(request, response)

        } catch (e: Exception) {
            handleException(e, response)
        }
    }

    //Allow swagger not using the filter
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.servletPath.contains("/swagger") || request.servletPath.contains("/api-docs")
    }

    //return http code, body to frontend when error
    fun handleException(ex: Exception, response: HttpServletResponse) {
        logger.error("handleException in ${this.javaClass.name}: ${ex.message}")
        response.status = HttpStatus.UNAUTHORIZED.value()
        ex.message?.let {
            response.writer.write(it)
        }
    }
}