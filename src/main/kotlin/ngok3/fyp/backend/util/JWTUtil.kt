package ngok3.fyp.backend.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtil(
    private val studentRoleEntityRepository: StudentRoleEntityRepository
) {
    @Value("\${cookie.lifetime}")
    val lifetime: String = "1"

    @Value("\${cookie.secretKey}")
    //For development test key, production key in application.yaml
    val secretKey: String = "U2WsdcBlHnnyNupc3z2vaq42Xbj9At5zG5TwTsIMldc="

    fun generateToken(studentEntity: StudentEntity): String {
        val claims: Claims = Jwts.claims()
        claims["itsc"] = studentEntity.itsc
        claims["name"] = studentEntity.nickname
        claims["mail"] = studentEntity.mail
        claims["role"] =
            studentEntity.studentRoleEntities.map { studentRoleEntity: StudentRoleEntity -> studentRoleEntity.societyEntity.name }


        //set token only valid in 7 days
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, Integer.parseInt(lifetime))
        claims.expiration = calendar.time

        return Jwts.builder().setClaims(claims).signWith(Keys.hmacShaKeyFor(secretKey.toByteArray())).compact()
    }

    fun verifyToken(jwtToken: String): Claims {
        //Verifying JWT is signed from our backend server
        try {
            return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray())).build()
                .parseClaimsJws(jwtToken).body
        } catch (e: JwtException) {
            throw e
        } catch (e: ExpiredJwtException) {
            print("JWT token is expired: ${e.message}")
            throw e
        }
    }

    fun verifyUserMemberRoleOfSociety(jwtToken: String, societyName: String) {
        val claims: Claims = verifyToken(jwtToken)
        val itsc: String = claims["itsc"].toString()

        studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(itsc, societyName, Role.ROLE_SOCIETY_MEMBER)
            .orElseThrow {
                throw AccessDeniedException("student with itsc: $itsc do not have ${Role.ROLE_SOCIETY_MEMBER} role of society: $societyName")
            }
    }

    fun getClaimFromJWTToken(jwtToken: String, key: String): String {
        val claims: Claims = verifyToken(jwtToken)
        return claims[key].toString()
    }
}