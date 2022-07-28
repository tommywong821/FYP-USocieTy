package ngok3.fyp.backend.authentication

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ngok3.fyp.backend.authentication.model.CasServiceResponse
import ngok3.fyp.backend.student.Student
import ngok3.fyp.backend.student.StudentRepository
import ngok3.fyp.backend.util.exception.model.CASException
import ngok3.fyp.backend.util.jwt.JWTUtil
import ngok3.fyp.backend.util.webclient.OkHttpClientFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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
        val student: Student
        try {
            val studentOptional: Optional<Student> =
                servicesResponse.authenticationSuccess!!.itsc!!.let { studentRepository.findByItsc(it) }
            //not exist: create new student and save in db
            if (studentOptional.isPresent) {
                student = studentOptional.get()
            } else {
                student = Student(
                    null,
                    servicesResponse.authenticationSuccess!!.itsc,
                    servicesResponse.authenticationSuccess!!.attributes!!.name,
                    servicesResponse.authenticationSuccess!!.attributes!!.mail
                )
                studentRepository.save(student)
            }
        } catch (e: NullPointerException) {
            throw CASException("authenticationFailure from HKUST CAS code: ${servicesResponse.authenticationFailure?.code} msg:${servicesResponse.authenticationFailure?.value}")
        }
        //return cookie to frontend with student information
        val cookie: Cookie = Cookie("token", jwtUtil.generateToken(student))
        //24 hours in second unit
        cookie.maxAge = 24 * 60 * 60
        frontendResponse.addCookie(cookie)

        return servicesResponse;
    }
}

