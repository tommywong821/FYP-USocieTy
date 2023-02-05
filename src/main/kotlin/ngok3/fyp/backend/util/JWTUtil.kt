package ngok3.fyp.backend.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JWTUtil(
    private var KEY: String = "dPyvYoiMuI8jUsIbqL-m-Fw-mMhc149ey4fkdBxQK9o",
    val secretKey: Key = Keys.hmacShaKeyFor(KEY.toByteArray()),
    @Autowired val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository
) {
    fun generateToken(studentEntity: StudentEntity): String {
        val claims: Claims = Jwts.claims()
        claims["itsc"] = studentEntity.itsc
        claims["name"] = studentEntity.nickname
        claims["mail"] = studentEntity.mail
        claims["role"] =
            studentEntity.roles.joinToString(separator = ",") { roleEntity: RoleEntity -> roleEntity.role.toString() }

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
        }
    }

    fun verifyUserEnrolledSociety(jwtToken: String, societyName: String) {
        val claims: Claims = verifyToken(jwtToken)
        val itsc: String = claims["itsc"].toString()

        if (enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                itsc,
                societyName,
                EnrolledStatus.SUCCESS
            ).isEmpty
        ) {
            throw AccessDeniedException("student with itsc: ${itsc} do not belong to this society: ${societyName}")
        }
    }
}