package ngok3.fyp.backend.operation.enrolledEvent

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledEventService
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_event_record.UpdateEnrolledEventRecordDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import java.util.*
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EnrolledEventControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var enrolledEventService: EnrolledEventService

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should update enroll event record status`() {
        val eventUUID: UUID = UUID.randomUUID()
        val studentUUID: UUID = UUID.randomUUID()

        every {
            enrolledEventService.updateEnrolledEventService(
                mockAuthRepository.validUserCookieToken,
                UpdateEnrolledEventRecordDto(
                    eventId = eventUUID.toString(),
                    studentId = studentUUID.toString(),
                    status = EnrolledStatus.SUCCESS
                )
            )
        } returns Unit

        mockMvc.put("/enrolledEventRecord") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }

            content = "{\"eventId\":\"$eventUUID\",\"studentId\":\"$studentUUID\",\"status\":\"SUCCESS\"}"
        }.andDo { print() }.andExpect { status { isNoContent() } }
    }
}