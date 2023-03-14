package ngok3.fyp.backend.operation.attendance

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.util.RSAUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.util.LinkedMultiValueMap
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AttendanceControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    private val rsaUtil: RSAUtil = RSAUtil()

    @MockkBean
    lateinit var attendanceService: AttendanceService

    @Test
    fun `should create attendance of student in event`() {
        val studentId: UUID = UUID.fromString("cead8c1e-7cbe-44c6-8fc1-dabe57c80168")
        val eventId: UUID = UUID.fromString("6c8180b4-0681-4d88-950f-c8f16859f9d6")

        every {
            attendanceService.createAttendance(
                studentId = studentId.toString(),
                eventId = eventId.toString(),
                userItsc = "itsc"
            )
        } returns Unit

        mockMvc.post("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            content =
                "{\"eventId\":\"6c8180b4-0681-4d88-950f-c8f16859f9d6\",\"studentId\":\"cead8c1e-7cbe-44c6-8fc1-dabe57c80168\",\"userItsc\":\"itsc\"}"
//            TODO enable encryption
//            content =
//                "{\"data\":\"dc3XrBxZHE0urCUzKlehtgXvLwl93WIgb+udouUxs2uxT56+ptMptD9I3VNxL3fTXGvyIXDlU6zVZcRuaMfV/6OBuCIylFigdChzngIg7WuEQ0kgtvP4aG/6eqyEz8eOdW/4czE+kxwYKq1yhC3y9PPwwY6fyhr0PZmV5jftXN4=\"}"
        }.andDo { print() }.andExpect { status { isCreated() } }
    }

    @Test
    fun `should get all attendance`() {
        val studentAttendanceDtoList: List<StudentAttendanceDto> = listOf(
            StudentAttendanceDto(
                UUID.randomUUID().toString(),
                "nickname 1",
                LocalDateTime.now().toString(),
                "test event 1"
            ),
            StudentAttendanceDto(
                UUID.randomUUID().toString(),
                "nickname 2",
                LocalDateTime.now().toString(),
                "test event 2"
            ),
        )

        every {
            attendanceService.getAllAttendance()
        } returns studentAttendanceDtoList

        mockMvc.get("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
        }.andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") {
                    isArray()
                }
                jsonPath("$.size()") {
                    value(studentAttendanceDtoList.size)
                }
                jsonPath("$[*].studentUuid") {
                    value(studentAttendanceDtoList.map { studentAttendance -> studentAttendance.studentUuid })
                }
                jsonPath("$[*].studentNickname") {
                    value(studentAttendanceDtoList.map { studentAttendance -> studentAttendance.studentNickname })
                }
                jsonPath("$[*].attendanceCreatedAt") {
                    value(studentAttendanceDtoList.map { studentAttendance -> studentAttendance.attendanceCreatedAt })
                }
                jsonPath("$[*].eventName") {
                    value(studentAttendanceDtoList.map { studentAttendance -> studentAttendance.eventName })
                }
            }
    }

    @Test
    fun `should delete attendance`() {
        val mockStudentUuid: String = UUID.randomUUID().toString()
        val mockEventUuid: String = UUID.randomUUID().toString()

        every {
            attendanceService.deleteAttendance(mockStudentUuid, mockEventUuid)
        } returns true

        mockMvc.delete("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("studentUuid", mockStudentUuid)
                add("eventUuid", mockEventUuid)
            }
        }.andDo { print() }.andExpect {
            status { isAccepted() }
        }
    }

    @Test
    fun `should not delete attendance`() {
        val mockStudentUuid: String = UUID.randomUUID().toString()
        val mockEventUuid: String = UUID.randomUUID().toString()

        every {
            attendanceService.deleteAttendance(mockStudentUuid, mockEventUuid)
        } returns false

        mockMvc.delete("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("studentUuid", mockStudentUuid)
                add("eventUuid", mockEventUuid)
            }
        }.andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }
}