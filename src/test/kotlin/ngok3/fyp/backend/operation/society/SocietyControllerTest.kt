package ngok3.fyp.backend.operation.society

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.student.StudentDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.util.LinkedMultiValueMap
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

        val memberOfSociety: List<StudentDto> = listOf<StudentDto>(
            StudentDto("qwerty", "nickname 1"),
            StudentDto("asdfg", "nickname 2"),
        )

        every {
            societyService.getAllSocietyMember(mockAuthRepository.testSocietyName)
        } returns memberOfSociety

        mockMvc.get("/society/member") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(memberOfSociety.size)
            }
            jsonPath("$[*].itsc") {
                value(memberOfSociety.map { studentDto -> studentDto.itsc })
            }
            jsonPath("$[*].nickname") {
                value(memberOfSociety.map { studentDto -> studentDto.nickname })
            }
        }
    }
}