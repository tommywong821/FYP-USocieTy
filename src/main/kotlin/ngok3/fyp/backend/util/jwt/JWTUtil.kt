package ngok3.fyp.backend.util.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import ngok3.fyp.backend.student.Student
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JWTUtil(
    private var KEY: String = "dPyvYoiMuI8jUsIbqL-m-Fw-mMhc149ey4fkdBxQK9o"
) {
    fun generateToken(student: Student): String {
        val claims: Claims = Jwts.claims()
        claims["itsc"] = student.itsc
        claims["name"] = student.name
        claims["mail"] = student.mail
        claims["role"] = student.role

        //set token only valid in 24 hours
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, 24)
        claims.expiration = calendar.time

//        Testing code
//        val keyString = generateSafeKey()
//        print("keyString: $keyString")
        val secretKey: Key = Keys.hmacShaKeyFor(KEY.toByteArray())

        return Jwts.builder().setClaims(claims).signWith(secretKey).compact()
    }

//    Testing code
//    fun generateSafeKey(): String{
//        val random: SecureRandom = SecureRandom()
//        //generate 256bit secrets
//        val bytes: ByteArray = ByteArray(32)
//
//        random.nextBytes(bytes)
//
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
//    }
}