package ngok3.fyp.backend.operation.finance

import io.jsonwebtoken.MalformedJwtException
import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.authentication.jwt.JWTUtil
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import java.util.*

@SpringBootTest
class FinanceServiceTest(
    @Autowired private val dateUtil: DateUtil,
    @Autowired private val jwtUtil: JWTUtil
) {
    private val financeEntityRepository: FinanceEntityRepository = mockk(relaxed = true)
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk(relaxed = true)
    private val financeService: FinanceService =
        FinanceService(financeEntityRepository, enrolledSocietyRecordRepository, dateUtil, jwtUtil)

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should get all finance tbale record from database with society name`() {

        every {
            financeEntityRepository.findFinanceTableDataWithSocietyName(
                "test society",
                dateUtil.convertStringToLocalDateTime("03-02-2023"),
                dateUtil.convertStringToLocalDateTime("04-02-2023")
            )
        } returns listOf<FinanceEntity>(
            FinanceEntity(1, "description 1", dateUtil.currentLocalDateTime),
            FinanceEntity(2.2, "description 2", dateUtil.currentLocalDateTime.plusDays(1)),
        )

        val financeTableData = financeService.getTableData(
            mockAuthRepository.validUserCookieToken,
            "test society",
            "03-02-2023",
            "04-02-2023"
        )

        assertEquals(financeTableData[0].amount, 1)
        assertEquals(financeTableData[0].description, "description 1")
        assertEquals(financeTableData[0].date, dateUtil.convertLocalDateTimeToString(dateUtil.currentLocalDateTime))

        assertEquals(financeTableData[1].amount, 2.2)
        assertEquals(financeTableData[1].description, "description 2")
        assertEquals(
            financeTableData[1].date,
            dateUtil.convertLocalDateTimeToString(dateUtil.currentLocalDateTime.plusDays(1))
        )
    }

    @Test
    fun `should not get all finance table record from database with itsc not joining the society`() {
        val itsc: String = "invalid itsc"
        val societyName: String = "test society"

        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                itsc,
                societyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.empty()

        val exception: AccessDeniedException = assertThrows {
            financeService.getTableData(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        }

        assertEquals("student with itsc: ${itsc} do not belong to this society: ${societyName}", exception.message)
    }

    @Test
    fun `should not get all finance table record from database with invalid jwt token`() {
        val exception: Exception = assertThrows {
            financeService.getTableData("dummy", "societyName", "03-02-2023", "04-02-2023")
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should get all finance pie chart record from database with society name`() {
        every {
            financeEntityRepository.findFinanceChartData(
                "test society",
                dateUtil.convertStringToLocalDateTime("05-02-2023"),
                dateUtil.convertStringToLocalDateTime("06-02-2023")
            )
        } returns listOf(
            FinanceChartDto("Souvenir", 5740),
            FinanceChartDto("Supplies", 4736)
        )

        val financePieChartData = financeService.getPieChartData("test society", "05-02-2023", "06-02-2023")

        assertEquals(financePieChartData[0].name, "Souvenir")
        assertEquals(financePieChartData[0].value, 5740)

        assertEquals(financePieChartData[1].name, "Supplies")
        assertEquals(financePieChartData[1].value, 4736)

    }
}