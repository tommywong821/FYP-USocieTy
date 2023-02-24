package ngok3.fyp.backend.operation.society

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.util.LinkedMultiValueMap
import java.util.*
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SocietyControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var societyService: SocietyService

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should get all student belong to society`() {

        val memberOfSociety: SocietyMemberDto = listOf<SocietyMemberDto>(
            SocietyMember()
        )

        every {
            societyService.getAllMembers(mockAuthRepository.testSocietyName)
        } returns Unit

        mockMvc.get("/society/member") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
            }
        }.andDo { print() }.andExpect { status { isNoContent() } }
    }
}