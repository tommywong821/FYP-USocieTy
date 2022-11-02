package ngok3.fyp.backend.authentication

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ngok3.fyp.backend.authentication.model.AuthenticationSuccess
import ngok3.fyp.backend.authentication.model.CasServiceResponse
import ngok3.fyp.backend.authentication.model.UserToken
import ngok3.fyp.backend.student.StudentEntity
import ngok3.fyp.backend.student.StudentRepository
import ngok3.fyp.backend.util.exception.model.CASException
import ngok3.fyp.backend.util.jwt.JWTUtil
import ngok3.fyp.backend.util.webclient.OkHttpClientFactory
import okhttp3.*
import okio.IOException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Service
class AuthService(
    private val webClient: OkHttpClient = OkHttpClientFactory().webClient,
    @Autowired val studentRepository: StudentRepository,
    @Autowired val jwtUtil: JWTUtil
) {
    @Value("\${heroku.frontend.url}")
    val frontendUrl: String? = null

    @Value("\${itsc.tenantId}")
    val tenantId: String = ""

    @Value("\${itsc.clientId}")
    val clientId: String = ""

    @Value("\${itsc.clientSecret}")
    val clientSecret: String = ""

    fun itscSSOServiceValidate(ticket: String, frontendResponse: HttpServletResponse): CasServiceResponse {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("cas.ust.hk")
            .addPathSegment("cas")
            .addPathSegment("p3")
            .addPathSegment("serviceValidate")
            .addQueryParameter("service", frontendUrl)
            .addQueryParameter("ticket", ticket)
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
        val cookie: Cookie = Cookie("token", jwtUtil.generateToken(studentEntity))
        //24 hours in second unit
        cookie.maxAge = 24 * 60 * 60
        frontendResponse.addCookie(cookie)

        return servicesResponse;
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
        return studentRepository.save(StudentEntity(itsc, name, mail))
    }

    fun mockItscSSOServiceValidate(ticket: String, frontendResponse: HttpServletResponse) {
        val studentEntity: StudentEntity =
            StudentEntity("dmchanxy", "CHAN, Dai Man", "dmchanxy@connect.ust.hk", "student")

        //return cookie to frontend with student information
        val cookie: Cookie = Cookie("token", jwtUtil.generateToken(studentEntity))
        //24 hours in second unit
        cookie.maxAge = 24 * 60 * 60
        frontendResponse.addCookie(cookie)
    }

    fun ROPCLogin(userinfo: Map<String, String>): String {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("login.microsoftonline.com")
            .addPathSegment(tenantId)
            .addPathSegment("oauth2")
            .addPathSegment("v2.0")
            .addPathSegment("token")
            .build()

        val requestBody: RequestBody = FormBody.Builder()
            .add("client_id", clientId)
            .add("scope", "user.read openid profile offline_access")
            .add("client_secret", clientSecret)
            .add("username", userinfo["username"].toString())
            .add("password", userinfo["password"].toString())
            .add("grant_type", "password").build()

        val request: Request =
            Request.Builder().header("Content-Type", "application/x-www-form-urlencoded").url(url).post(requestBody)
                .build()
        val response: Response = webClient.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }
        return response.body!!.string()
    }

    fun validateMobileLogin(userInfo: Map<String, String>): UserToken {
        //find student by itsc in db
        val studentEntity: StudentEntity
        try {
            val studentEntityOptional: Optional<StudentEntity> =
                studentRepository.findByItsc(userInfo["itsc"].toString())
            //not exist: create new student and save in db
            studentEntity =
                studentEntityOptional.orElseGet {
                    createNewStudentEntityInDB(
                        userInfo["itsc"].toString(),
                        userInfo["name"].toString(),
                        userInfo["email"].toString()
                    )
                }

            return UserToken(jwtUtil.generateToken(studentEntity))
        } catch (e: NullPointerException) {
            throw e
        }
    }
}

