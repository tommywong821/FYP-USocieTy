package ngok3.fyp.backend.operation.enrolledEvent

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordService
import ngok3.fyp.backend.operation.enrolled.event_record.PaymentStatus
import ngok3.fyp.backend.operation.enrolled.event_record.model.StudentEnrolledEventRecordDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.UpdateEnrolledEventRecordDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.util.LinkedMultiValueMap
import java.util.*
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EnrolledEventControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var enrolledEventService: EnrolledEventRecordService

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should update enroll event record status`() {
        val eventUUID: UUID = UUID.randomUUID()
        val studentUUID: UUID = UUID.randomUUID()

        every {
            enrolledEventService.updateEnrolledEventRecord(
                mockAuthRepository.validUserCookieToken,
                listOf(
                    UpdateEnrolledEventRecordDto(
                        eventId = eventUUID.toString(),
                        studentId = studentUUID.toString(),
                        enrolledStatus = EnrolledStatus.SUCCESS,
                        paymentStatus = PaymentStatus.PAID
                    )
                )
            )
        } returns Unit

        mockMvc.put("/enrolledEventRecord") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }

            content =
                "[{\"eventId\":\"$eventUUID\",\"studentId\":\"$studentUUID\",\"enrolledStatus\":\"SUCCESS\",\"paymentStatus\":\"PAID\"}]"
        }.andDo { print() }.andExpect { status { isOk() } }
    }

    @Test
    fun `should get all student with enrolled status with event id`() {
        val eventUuid: String = UUID.randomUUID().toString()

        val studentEnrolledEventRecordDtoList: List<StudentEnrolledEventRecordDto> = listOf(
            StudentEnrolledEventRecordDto(
                studentId = UUID.randomUUID().toString(),
                itsc = "qwerty",
                paymentStatus = PaymentStatus.UNPAID.status,
                enrolledStatus = EnrolledStatus.SUCCESS.status
            ),
            StudentEnrolledEventRecordDto(
                studentId = UUID.randomUUID().toString(),
                itsc = "asdfg",
                paymentStatus = PaymentStatus.PAID.status,
                enrolledStatus = EnrolledStatus.PENDING.status
            ),
            StudentEnrolledEventRecordDto(
                studentId = UUID.randomUUID().toString(),
                itsc = "qwerty",
                paymentStatus = PaymentStatus.UNPAID.status,
                enrolledStatus = EnrolledStatus.DECLINE.status
            ),
        )

        every {
            enrolledEventService.getStudentEnrolledEventRecord(
                mockAuthRepository.validUserCookieToken,
                eventUuid,
                0, 10
            )
        } returns studentEnrolledEventRecordDtoList

        mockMvc.get("/enrolledEventRecord/$eventUuid") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("pageNum", "")
                add("pageIndex", "")
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(studentEnrolledEventRecordDtoList.size)
            }
            jsonPath("$[*].studentId") {
                value(studentEnrolledEventRecordDtoList.map { studentEnrolledEventRecordDto -> studentEnrolledEventRecordDto.studentId })
            }
            jsonPath("$[*].itsc") {
                value(studentEnrolledEventRecordDtoList.map { studentEnrolledEventRecordDto -> studentEnrolledEventRecordDto.itsc })
            }
            jsonPath("$[*].paymentStatus") {
                value(studentEnrolledEventRecordDtoList.map { studentEnrolledEventRecordDto -> studentEnrolledEventRecordDto.paymentStatus })
            }
            jsonPath("$[*].enrolledStatus") {
                value(studentEnrolledEventRecordDtoList.map { studentEnrolledEventRecordDto -> studentEnrolledEventRecordDto.enrolledStatus })
            }
        }
    }
}