package ngok3.fyp.backend.operation.enrolledSociety

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordService
import ngok3.fyp.backend.operation.enrolled.society_record.UpdateEnrolledSocietyRecordDto
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
class EnrolledSocietyControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var enrolledSocietyService: EnrolledSocietyRecordService

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should update enroll society record status`() {
        val societyUUID: UUID = UUID.randomUUID()
        val studentUUID: UUID = UUID.randomUUID()

        every {
            enrolledSocietyService.updateEnrolledSocietyRecord(
                mockAuthRepository.validUserCookieToken,
                UpdateEnrolledSocietyRecordDto(
                    societyId = societyUUID.toString(),
                    studentId = studentUUID.toString(),
                    status = EnrolledStatus.SUCCESS
                )
            )
        } returns Unit

        mockMvc.put("/enrolledSocietyRecord") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }

            content = "{\"societyId\":\"$societyUUID\",\"studentId\":\"$studentUUID\",\"status\":\"SUCCESS\"}"
        }.andDo { print() }.andExpect { status { isNoContent() } }
    }
}