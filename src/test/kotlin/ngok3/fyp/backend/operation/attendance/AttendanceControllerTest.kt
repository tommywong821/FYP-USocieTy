package ngok3.fyp.backend.operation.attendance

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.util.RSAUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
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
            attendanceService.createAttendance(studentId = studentId.toString(), eventId = eventId.toString())
        } returns Unit

        mockMvc.post("/attendance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            content =
                "{\"data\":\"JAtvHsv2DYgtfqmpA6OrIGh19TfvdU2U\\/tpylZS2E+KLZoxpbLIJvcFbWhP\\/87nKXDr46PKBaTVEfmYs4eQL77pr+83UFJEaf\\/mo6eb6M\\/kZkf9EIAeHmLWaqU14CzoO0nNWRBbDyRmbPHujA3kgkLIcIcbFIbxYSz9pbpIa460=\"}"
        }.andDo { print() }.andExpect { status { isCreated() } }
    }
}