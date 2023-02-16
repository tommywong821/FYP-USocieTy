package ngok3.fyp.backend.authentication

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ngok3.fyp.backend.authentication.model.*
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.role.RoleEntityRepository
import ngok3.fyp.backend.operation.student.StudentDto
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.JWTUtil
import ngok3.fyp.backend.util.exception.model.CASException
import ngok3.fyp.backend.util.webclient.OkHttpClientFactory
import okhttp3.*
import okio.IOException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*
import javax.servlet.http.HttpServletResponse


@Service
class AuthService(
    private val webClient: OkHttpClient = OkHttpClientFactory().webClient,
    private val studentRepository: StudentRepository,
    private val roleEntityRepository: RoleEntityRepository,
    private val jwtUtil: JWTUtil
) {
    @Value("\${heroku.frontend.url}")
    val frontendUrl: String? = null

    @Value("\${cookie.lifetime}")
    val lifetime: String = "1"

    fun itscSSOServiceValidate(ticket: Map<String, String>, frontendResponse: HttpServletResponse): StudentDto {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("cas.ust.hk")
            .addPathSegment("cas")
            .addPathSegment("p3")
            .addPathSegment("serviceValidate")
            .addQueryParameter("service", frontendUrl)
            .addQueryParameter("ticket", ticket["ticket"])
            .build()
        val request: Request = Request.Builder().url(url).build()
        //get user info from ust cas server
        val response: Response = webClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
        //change body xml string to kotlin object
        val servicesResponse: CasServiceResponse = XmlMapper().readValue(response.body!!.string())

        //find student by itsc in db
        val studentEntity: StudentEntity
        try {
            val studentEntityOptional: Optional<StudentEntity> =
                servicesResponse.authenticationSuccess!!.itsc!!.let { studentRepository.findByItsc(it) }
            //not exist: create new student and save in db
            studentEntity =
                studentEntityOptional.orElseGet { createNewStudentEntityInDB(servicesResponse.authenticationSuccess!!) }
        } catch (e: NullPointerException) {
            throw CASException("authenticationFailure from HKUST CAS code: ${servicesResponse.authenticationFailure?.code} msg:${servicesResponse.authenticationFailure?.value}")
        }
        //return cookie to frontend with student information
        val cookie: ResponseCookie =
            ResponseCookie.from("token", jwtUtil.generateToken(studentEntity)).httpOnly(true).secure(true).path("/")
                .maxAge(
                    Duration.ofDays(lifetime.toLong())
                ).sameSite("None").build()
        frontendResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        //create society list string
        val enrolledSocietyList: List<String> =
            studentEntity.enrolledSocietyRecordEntity.map { it.societyEntity.name }
        // create role list string
        val roleList: List<String> = studentEntity.roles.map { it.role.toString() }
        return StudentDto(studentEntity, enrolledSocietyList, roleList)
    }

    fun createNewStudentEntityInDB(authenticationSuccess: AuthenticationSuccess): StudentEntity {
        val itsc = authenticationSuccess.itsc ?: throw NullPointerException("itsc cannot be null to create new record")
        val name = authenticationSuccess.attributes?.name
            ?: throw NullPointerException("name cannot be null to create new record")
        val mail = authenticationSuccess.attributes?.mail
            ?: throw NullPointerException("mail cannot be null to create new record")
        return createNewStudentEntityInDB(itsc, name, mail)
    }

    fun createNewStudentEntityInDB(itsc: String, name: String, mail: String): StudentEntity {
        val newStudentEntity = StudentEntity(itsc, name, mail)
        newStudentEntity.roles = mutableSetOf(roleEntityRepository.findByRole(Role.ROLE_STUDENT))
        return studentRepository.save(newStudentEntity)
    }

    fun mockItscSSOServiceValidate(
        ticket: Map<String, String>,
        frontendResponse: HttpServletResponse
    ): StudentDto {
        print(ticket["ticket"])
        val studentEntity: StudentEntity =
            studentRepository.findByItsc("tkwongax").get()

        //return cookie to frontend with student information
        val cookie: ResponseCookie =
            ResponseCookie.from("token", jwtUtil.generateToken(studentEntity)).httpOnly(true).secure(true).path("/")
                .maxAge(
                    Duration.ofDays(lifetime.toLong())
                ).sameSite("None").build()
        frontendResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())


        //create society list string
        val enrolledSocietyList: List<String> =
            studentEntity.enrolledSocietyRecordEntity.map { it.societyEntity.name }
        // create role list string
        val roleList: List<String> = studentEntity.roles.map { it.role.toString() }

        val mockResponse = CasServiceResponse()
        mockResponse.authenticationFailure = AuthenticationFailure(null, null)
        mockResponse.authenticationSuccess =
            AuthenticationSuccess("tkwongax", CasAttributes("tkwongax@connect.ust.hk", "WONG, Tsz Kit"))
        return StudentDto(studentEntity, enrolledSocietyList, roleList)
    }

    fun validateMobileLogin(aadProfile: AADProfile): UserToken {
        //find student by itsc in db
        val studentEntity: StudentEntity
        try {
            val studentEntityOptional: Optional<StudentEntity> =
                studentRepository.findByItsc(aadProfile.itsc)
            //not exist: create new student and save in db
            studentEntity =
                studentEntityOptional.orElseGet {
                    createNewStudentEntityInDB(
                        aadProfile.itsc,
                        aadProfile.email,
                        aadProfile.name
                    )
                }

            return UserToken(jwtUtil.generateToken(studentEntity))
        } catch (e: NullPointerException) {
            throw e
        }
    }

    fun logout(frontendResponse: HttpServletResponse): ResponseEntity<HashMap<String, String>> {
//        clear frontend cookie
        val cookie: ResponseCookie =
            ResponseCookie.from("token", "").httpOnly(true).secure(true).path("/")
                .maxAge(0).sameSite("None").build()
        frontendResponse.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())

        return ResponseEntity(hashMapOf("message" to "You are logged out"), HttpStatus.OK)
    }
}

