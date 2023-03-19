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
import org.apache.commons.lang3.StringUtils
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JWTUtil(
    private var KEY: String = "dPyvYoiMuI8jUsIbqL-m-Fw-mMhc149ey4fkdBxQK9o",
    val secretKey: Key = Keys.hmacShaKeyFor(KEY.toByteArray()),
    private val studentRoleEntityRepository: StudentRoleEntityRepository
) {
    fun generateToken(studentEntity: StudentEntity): String {
        val claims: Claims = Jwts.claims()
        claims["itsc"] = studentEntity.itsc
        claims["name"] = studentEntity.nickname
        claims["mail"] = studentEntity.mail
        val role =
            studentEntity.studentRoleEntities.joinToString(separator = ",") { studentRoleEntity: StudentRoleEntity -> studentRoleEntity.roleEntity.role.toString() }
        claims["role"] = if (StringUtils.isBlank(role)) Role.ROLE_STUDENT.toString() else role


        //set token only valid in 24 hours
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, 24)
        claims.expiration = calendar.time

        return Jwts.builder().setClaims(claims).signWith(secretKey).compact()
    }

    fun verifyToken(jwtToken: String): Claims {
        //Verifying JWT is signed from our backend server
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).body
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