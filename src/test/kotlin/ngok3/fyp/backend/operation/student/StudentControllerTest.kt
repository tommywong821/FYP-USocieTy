package ngok3.fyp.backend.operation.student

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.TotalCountDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.student.model.StudentDto
import ngok3.fyp.backend.operation.student.model.StudentEnrolledSocietyStatusDto
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val mockStudentEntity = mockStudentRepository.testStudentEntity

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()
    private val dateUtil: DateUtil = DateUtil()

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

    @Test
    fun `should return student with society status`() {
        every { studentService.getStudentSocietyStatus("qwert") } returns listOf<StudentEnrolledSocietyStatusDto>(
            StudentEnrolledSocietyStatusDto(
                societyName = "test society 1", registerDate = dateUtil.convertLocalDateTimeToStringWithTime(
                    LocalDateTime.now()
                ), enrolledStatus = EnrolledStatus.PENDING
            ),
            StudentEnrolledSocietyStatusDto(
                societyName = "test society 2", registerDate = dateUtil.convertLocalDateTimeToStringWithTime(
                    LocalDateTime.now()
                ), enrolledStatus = EnrolledStatus.SUCCESS
            )
        )

        val result: List<StudentEnrolledSocietyStatusDto> = studentService.getStudentSocietyStatus("qwert")

        mockMvc.get("/student/societyStatus?itsc=qwert")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[*].societyName") {
                    value(result.map { studentEnrolledSocietyStatusDto: StudentEnrolledSocietyStatusDto -> studentEnrolledSocietyStatusDto.societyName })
                }
                jsonPath("$[*].registerDate") {
                    value(result.map { studentEnrolledSocietyStatusDto: StudentEnrolledSocietyStatusDto -> studentEnrolledSocietyStatusDto.registerDate })
                }
                jsonPath("$[*].enrolledStatus") {
                    value(result.map { studentEnrolledSocietyStatusDto: StudentEnrolledSocietyStatusDto -> studentEnrolledSocietyStatusDto.enrolledStatus.toString() })
                }
            }
    }

    @Test
    fun `should return total number of enrolled event by student id`() {
        every {
            studentService.countAllEventWithSocietyMember(
                mockAuthRepository.validUserCookieToken,
                "qwert"
            )
        } returns TotalCountDto(23)

        mockMvc.get("/student/{studentId}/event/totalNumber", "qwert") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.totalNumber") {
                    value(23)
                }
            }
    }
}