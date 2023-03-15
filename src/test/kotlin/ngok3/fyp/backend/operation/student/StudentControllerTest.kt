package ngok3.fyp.backend.operation.student

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val mockStudentEntity = mockStudentRepository.testStudentEntity

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @MockkBean
    lateinit var studentService: StudentService

    @Test
    @DisplayName("GET /student?itsc={itsc}")
    fun `should return test student profile with itsc`() {
        every { studentService.getStudentProfile(mockStudentRepository.testItsc, "", "") } returns StudentDto(
            mockStudentEntity.uuid.toString(),
            mockStudentEntity.itsc,
            mockStudentEntity.nickname,
            mockStudentEntity.mail,
        )

        mockMvc.get("/student?itsc=${mockStudentRepository.testItsc}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.uuid") {
                    value(mockStudentRepository.testStudentEntity.uuid.toString())
                }
                jsonPath("$.itsc") {
                    value(mockStudentRepository.testStudentEntity.itsc)
                }
                jsonPath("$.nickname") {
                    value(mockStudentRepository.testStudentEntity.nickname)
                }
                jsonPath("$.mail") {
                    value(mockStudentRepository.testStudentEntity.mail)
                }
            }
    }

    @Test
    @DisplayName("GET /student?uuid={mockUuid}")
    fun `should return test student profile with uuid`() {
        val mockUuid: String = UUID.randomUUID().toString()
        every { studentService.getStudentProfile("", mockUuid, "") } returns StudentDto(
            mockStudentEntity.uuid.toString(),
            mockStudentEntity.itsc,
            mockStudentEntity.nickname,
            mockStudentEntity.mail,
        )

        mockMvc.get("/student?uuid=${mockUuid}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.uuid") {
                    value(mockStudentRepository.testStudentEntity.uuid.toString())
                }
                jsonPath("$.itsc") {
                    value(mockStudentRepository.testStudentEntity.itsc)
                }
                jsonPath("$.nickname") {
                    value(mockStudentRepository.testStudentEntity.nickname)
                }
                jsonPath("$.mail") {
                    value(mockStudentRepository.testStudentEntity.mail)
                }
            }
    }

    @Test
    @DisplayName("GET /student?cardId={cardId}")
    fun `should return test student profile with cardId`() {
        every { studentService.getStudentProfile("", "", "cardId") } returns StudentDto(
            mockStudentEntity.uuid.toString(),
            mockStudentEntity.itsc,
            mockStudentEntity.nickname,
            mockStudentEntity.mail,
        )

        mockMvc.get("/student?cardId=cardId")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.uuid") {
                    value(mockStudentRepository.testStudentEntity.uuid.toString())
                }
                jsonPath("$.itsc") {
                    value(mockStudentRepository.testStudentEntity.itsc)
                }
                jsonPath("$.nickname") {
                    value(mockStudentRepository.testStudentEntity.nickname)
                }
                jsonPath("$.mail") {
                    value(mockStudentRepository.testStudentEntity.mail)
                }
            }
    }
}