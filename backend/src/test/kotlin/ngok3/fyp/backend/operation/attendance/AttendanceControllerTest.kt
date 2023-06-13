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
                userItsc = "itsc",
                currentTime = "2023-03-24T15:33:23.123Z"
            )
        } returns Unit

        mockMvc.post("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            content =
                "YonFwpXCcymehFJgXy/KLlMvPBi3m2g1dYwry0R1KUmz6sFJcRJrtj3LPKhf5xUgfwkvDw0Z63lGX+VRbQF9SiJCHt7lZVDKeyTVypXAP/OMjaFkDVqaX9l1OnKgzcqzAq1w939UykkqSetKZqBrFDWfHwAQGAzJbJL0HIT8Sj/o7jMXUCIU3l6xRsgyuWQPurWpqGHoJhxiSOQdsrRUQnpfmsB4XSd84vd7Bk8pwcZmdCpFubIGwm6BMV66m9S+A49mOEyK/KWcpCsrH1ELa2G0X9u9lsdGYdqcWM29pWNROjN3KyptN5VvQeODF9xSuLw2h1xOWblbQUgIy2W62w=="
        }.andDo { print() }.andExpect { status { isCreated() } }
    }

    @Test
    fun `should get all attendance`() {
        val studentAttendanceDtoList: List<StudentAttendanceDto> = listOf(
            StudentAttendanceDto(
                studentUuid = UUID.randomUUID().toString(),
                studentItsc = "nickname 1",
                attendanceCreatedAt = LocalDateTime.now().toString(),
                eventName = "test event 1"
            ),
            StudentAttendanceDto(
                studentUuid = UUID.randomUUID().toString(),
                studentItsc = "nickname 2",
                attendanceCreatedAt = LocalDateTime.now().toString(),
                eventName = "test event 2"
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
                jsonPath("$[*].studentItsc") {
                    value(studentAttendanceDtoList.map { studentAttendance -> studentAttendance.studentItsc })
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
        val mockStudentUuid: String = "f4d097c3-7e28-464e-8f44-205380d4ae60"
        val mockEventUuid: String = "faded982-91d2-42f0-a745-5cbee94b67cf"

        every {
            attendanceService.deleteAttendance(mockStudentUuid, mockEventUuid)
        } returns true

        mockMvc.delete("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add(
                    "studentUuid",
                    "VhCVAcOfRIivipOT3syiRJy1budLt7WPX3TxUyDQIC5hGV/d356CpAQ7ASeIT2L+yErB6ptaiAxsalxvmGsfho/2SOy0GUQIPqY0DpdGbhGkSIoSVTxvpO4u93e8EPKgbiLHCleM9yWJXLYAZZbiqxzaBK+0e02aLmhnHUzYwjobd++RgybmwoA3cUOYFtSrIjI8udXtWFsGWTZLtOv0RdUdLeTMAYh89hp6UIkKJvXepEE7qcg6Cn4xCBLCab0FruEvBxZh/rdhw96u+qu03xvfLqQPAYoDghYM4kks8fGM+p1mvVanxmRqYVpNKWgOWqSeWsVN+VplbHkf5gzL3w=="
                )
                add(
                    "eventUuid",
                    "FmOBeBsKjSc4f/cwDGQ7QXZ3NNXGIkxg4rAh6gJoeo4K81KuPyRXAaIGTGsfX8ND31y0ws0Y1klP065Coaac0/mG/X6tz4UdOEAxeUSfFxTylL/OkBUZ+ZppwswprrnJsZq0AvcfV+YfyNHeUg8ooAw2xlEVNKB/pqthyT8t0H71JgK76TddqparqTT912fDFeEaQYp+HmpXkmqMjbaMtP3jnHKtwEPsTJz7tRpksg99p3jI4kPznp+TMv6BRVjjjfuyzZZVh7uw0IC2OI61pcB95FpgRtKOCIKsSWrkPtkh+5RTyCz6pum+n3gutpmvtmBLPdWQkTmcyUaza437kg=="
                )
            }
        }.andDo { print() }.andExpect {
            status { isAccepted() }
        }
    }

    @Test
    fun `should not delete attendance`() {
        val mockStudentUuid: String = "f4d097c3-7e28-464e-8f44-205380d4ae60"
        val mockEventUuid: String = "faded982-91d2-42f0-a745-5cbee94b67cf"

        every {
            attendanceService.deleteAttendance(mockStudentUuid, mockEventUuid)
        } returns false

        mockMvc.delete("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add(
                    "studentUuid",
                    "VhCVAcOfRIivipOT3syiRJy1budLt7WPX3TxUyDQIC5hGV/d356CpAQ7ASeIT2L+yErB6ptaiAxsalxvmGsfho/2SOy0GUQIPqY0DpdGbhGkSIoSVTxvpO4u93e8EPKgbiLHCleM9yWJXLYAZZbiqxzaBK+0e02aLmhnHUzYwjobd++RgybmwoA3cUOYFtSrIjI8udXtWFsGWTZLtOv0RdUdLeTMAYh89hp6UIkKJvXepEE7qcg6Cn4xCBLCab0FruEvBxZh/rdhw96u+qu03xvfLqQPAYoDghYM4kks8fGM+p1mvVanxmRqYVpNKWgOWqSeWsVN+VplbHkf5gzL3w=="
                )
                add(
                    "eventUuid",
                    "FmOBeBsKjSc4f/cwDGQ7QXZ3NNXGIkxg4rAh6gJoeo4K81KuPyRXAaIGTGsfX8ND31y0ws0Y1klP065Coaac0/mG/X6tz4UdOEAxeUSfFxTylL/OkBUZ+ZppwswprrnJsZq0AvcfV+YfyNHeUg8ooAw2xlEVNKB/pqthyT8t0H71JgK76TddqparqTT912fDFeEaQYp+HmpXkmqMjbaMtP3jnHKtwEPsTJz7tRpksg99p3jI4kPznp+TMv6BRVjjjfuyzZZVh7uw0IC2OI61pcB95FpgRtKOCIKsSWrkPtkh+5RTyCz6pum+n3gutpmvtmBLPdWQkTmcyUaza437kg=="
                )
            }
        }.andDo { print() }.andExpect {
            status { isNotFound() }
        }
    }
}