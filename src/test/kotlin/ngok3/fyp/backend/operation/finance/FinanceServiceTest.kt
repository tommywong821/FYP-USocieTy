package ngok3.fyp.backend.operation.finance

import io.jsonwebtoken.MalformedJwtException
import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
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
) {
    private val financeEntityRepository: FinanceEntityRepository = mockk(relaxed = true)
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk(relaxed = true)
    private val jwtUtil: JWTUtil = JWTUtil(enrolledSocietyRecordRepository = enrolledSocietyRecordRepository)
    private val financeService: FinanceService =
        FinanceService(financeEntityRepository, dateUtil, jwtUtil)

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should get all finance table record from database with society name`() {
        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSociety,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            financeEntityRepository.findFinanceTableDataWithSocietyName(
                mockAuthRepository.testSociety,
                dateUtil.convertStringToLocalDateTime("03-02-2023"),
                dateUtil.convertStringToLocalDateTime("04-02-2023")
            )
        } returns listOf<FinanceEntity>(
            FinanceEntity(1, "description 1", dateUtil.currentLocalDateTime),
            FinanceEntity(2.2, "description 2", dateUtil.currentLocalDateTime.plusDays(1)),
        )

        val financeTableData = financeService.getTableData(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSociety,
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
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSociety

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
            financeService.getTableData("dummy", mockAuthRepository.testSociety, "03-02-2023", "04-02-2023")
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should get all finance pie chart record from database with society name`() {
        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSociety,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            financeEntityRepository.findFinanceChartData(
                mockAuthRepository.testSociety,
                dateUtil.convertStringToLocalDateTime("05-02-2023"),
                dateUtil.convertStringToLocalDateTime("06-02-2023")
            )
        } returns listOf(
            FinanceChartDto("Souvenir", 5740),
            FinanceChartDto("Supplies", 4736)
        )

        val financePieChartData = financeService.getPieChartData(
            mockAuthRepository.validUserCookieToken,
            "test society",
            "05-02-2023",
            "06-02-2023"
        )

        assertEquals(financePieChartData[0].name, "Souvenir")
        assertEquals(financePieChartData[0].value, 5740)

        assertEquals(financePieChartData[1].name, "Supplies")
        assertEquals(financePieChartData[1].value, 4736)

    }
}