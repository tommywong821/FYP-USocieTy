package ngok3.fyp.backend.student

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val mockStudentEntity = mockStudentRepository.testStudentEntity

    @MockkBean
    lateinit var studentService: StudentService

    @Test
    @DisplayName("GET /student?itsc={itsc}")
    fun `should return test student profile`() {
        every { studentService.getStudentProfile(mockStudentRepository.testItsc) } returns StudentDto(
            mockStudentEntity.itsc,
            mockStudentEntity.nickname,
            mockStudentEntity.mail,
            mockStudentEntity.role
        )

        mockMvc.get("/student?itsc=${mockStudentRepository.testItsc}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.itsc") {
                    value(mockStudentRepository.testStudentEntity.itsc)
                }
                jsonPath("$.nickname") {
                    value(mockStudentRepository.testStudentEntity.nickname)
                }
                jsonPath("$.mail") {
                    value(mockStudentRepository.testStudentEntity.mail)
                }
                jsonPath("$.role") {
                    value(mockStudentRepository.testStudentEntity.role)
                }
            }
    }
}