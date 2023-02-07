package ngok3.fyp.backend.operation.finance

import io.jsonwebtoken.MalformedJwtException
import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.finance.model.CreateFinanceDto
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceRecordDto
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
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
    private val studentRepository: StudentRepository = mockk(relaxed = true)
    private val societyRepository: SocietyRepository = mockk(relaxed = true)
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk(relaxed = true)
    private val jwtUtil: JWTUtil = JWTUtil(enrolledSocietyRecordRepository = enrolledSocietyRecordRepository)
    private val financeService: FinanceService =
        FinanceService(financeEntityRepository, studentRepository, societyRepository, dateUtil, jwtUtil)

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should get all finance table record from database with society name`() {
        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            financeEntityRepository.findFinanceTableDataWithSocietyName(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("3/2/2023"),
                dateUtil.convertStringToLocalDateTime("4/2/2023")
            )
        } returns listOf<FinanceEntity>(
            FinanceEntity(1.0, "description 1", dateUtil.currentLocalDateTime),
            FinanceEntity(2.2, "description 2", dateUtil.currentLocalDateTime.plusDays(1)),
        )

        val financeTableData = financeService.getTableData(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            "3/2/2023",
            "4/2/2023"
        )

        assertEquals(financeTableData[0].amount, 1.0)
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
        val societyName: String = mockAuthRepository.testSocietyName

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
            financeService.getTableData("dummy", mockAuthRepository.testSocietyName, "03-02-2023", "04-02-2023")
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should get all finance pie chart record from database with society name`() {
        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            financeEntityRepository.findFinancePieChartData(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("5/2/2023"),
                dateUtil.convertStringToLocalDateTime("6/2/2023")
            )
        } returns listOf(
            FinanceChartDto("Souvenir", 5740.0),
            FinanceChartDto("Supplies", 4736.0)
        )

        val financePieChartData = financeService.getPieChartData(
            mockAuthRepository.validUserCookieToken,
            "test society",
            "5/2/2023",
            "6/2/2023"
        )

        assertEquals(financePieChartData[0].name, "Souvenir")
        assertEquals(financePieChartData[0].value, 5740.0)

        assertEquals(financePieChartData[1].name, "Supplies")
        assertEquals(financePieChartData[1].value, 4736.0)

    }

    @Test
    fun `should not get all finance pie chart record from database with itsc not joining the society`() {
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSocietyName

        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                itsc,
                societyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.empty()

        val exception: AccessDeniedException = assertThrows {
            financeService.getPieChartData(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        }

        assertEquals("student with itsc: ${itsc} do not belong to this society: ${societyName}", exception.message)
    }

    @Test
    fun `should not get all finance pie chart record from database with invalid jwt token`() {
        val exception: Exception = assertThrows {
            financeService.getPieChartData("dummy", mockAuthRepository.testSocietyName, "03-02-2023", "04-02-2023")
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should get all finance bar chart record from database with society name`() {
        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            financeEntityRepository.findFinanceBarChartData(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("5/2/2023"),
                dateUtil.convertStringToLocalDateTime("6/2/2023")
            )
        } returns listOf(
            FinanceChartDto("Jan-2023", 7187.0),
            FinanceChartDto("Feb-2023", 8738.0)
        )

        val financePieChartData = financeService.getBarChartData(
            mockAuthRepository.validUserCookieToken,
            "test society",
            "5/2/2023",
            "6/2/2023"
        )

        assertEquals(financePieChartData[0].name, "Jan-2023")
        assertEquals(financePieChartData[0].value, 7187.0)

        assertEquals(financePieChartData[1].name, "Feb-2023")
        assertEquals(financePieChartData[1].value, 8738.0)

    }

    @Test
    fun `should not get all finance bar chart record from database with itsc not joining the society`() {
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSocietyName

        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                itsc,
                societyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.empty()

        val exception: AccessDeniedException = assertThrows {
            financeService.getBarChartData(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "3/2/2023",
                "4/2/2023"
            )
        }

        assertEquals("student with itsc: ${itsc} do not belong to this society: ${societyName}", exception.message)
    }

    @Test
    fun `should not get all finance bar chart record from database with invalid jwt token`() {
        val exception: Exception = assertThrows {
            financeService.getBarChartData("dummy", mockAuthRepository.testSocietyName, "3/2/2023", "4/2/2023")
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should create finance record with society name`() {
        val studentEntity: StudentEntity = StudentEntity(
            mockAuthRepository.validUserItsc,
            mockAuthRepository.validUserNickname,
            mockAuthRepository.validUserMail
        )
        every { studentRepository.findByItsc(mockAuthRepository.validUserItsc) } returns Optional.of(studentEntity)

        val societyEntity: SocietyEntity = SocietyEntity(mockAuthRepository.testSocietyName)
        every { societyRepository.findByName(mockAuthRepository.testSocietyName) } returns Optional.of(societyEntity)

        val financeEntityList: List<FinanceEntity> = listOf<FinanceEntity>(
            FinanceEntity(12.3, "aaa", dateUtil.convertStringToLocalDateTime("2/7/2023"), "test category 1"),
            FinanceEntity(34.5, "bbb", dateUtil.convertStringToLocalDateTime("2/5/2023"), "test category 2")
        )

        val financeChartDto: CreateFinanceDto = CreateFinanceDto(
            "test society",
            listOf(
                FinanceRecordDto(12.3, "test category 1", "aaa", "2/7/2023"),
                FinanceRecordDto(34.5, "test category 2", "bbb", "2/5/2023"),
            )
        )

        for (financeEntity in financeEntityList) {
            financeEntity.societyEntity = societyEntity
            financeEntity.studentEntity = studentEntity
        }

        every { financeEntityRepository.saveAll(any<List<FinanceEntity>>()) } returns financeEntityList

        val financeCreateRecord =
            financeService.createFinancialRecords(mockAuthRepository.validUserCookieToken, financeChartDto)

        assertEquals(financeEntityList.size, financeCreateRecord.size)
    }
}