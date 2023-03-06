package ngok3.fyp.backend.operation.finance

import io.jsonwebtoken.MalformedJwtException
import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.finance.model.*
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
    private val financeEntityDao: FinanceEntityDao = mockk(relaxed = true)
    private val studentRepository: StudentRepository = mockk(relaxed = true)
    private val societyRepository: SocietyRepository = mockk(relaxed = true)
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk()
    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)
    private val financeService: FinanceService =
        FinanceService(
            financeEntityRepository,
            financeEntityDao,
            studentRepository,
            societyRepository,
            dateUtil,
            jwtUtil
        )

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should get all finance table record from database with society name`() {
        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityDao.findFinanceTableDataWithSocietyNameWithPageAngFilter(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("3/2/2023"),
                dateUtil.convertStringToLocalDateTime("4/2/2023"),
                1,
                10,
                "",
                false,
                emptyList()
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
    fun `should get pageable finance table record from database with society name`() {
        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityDao.findFinanceTableDataWithSocietyNameWithPageAngFilter(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("3/2/2023"),
                dateUtil.convertStringToLocalDateTime("4/2/2023"),
                2,
                5,
                "",
                false,
                emptyList()
            )
        } returns listOf<FinanceEntity>(
            FinanceEntity(1.0, "description 1", dateUtil.currentLocalDateTime),
            FinanceEntity(2.2, "description 2", dateUtil.currentLocalDateTime.plusDays(1)),
        )

        val financeTableData = financeService.getTableData(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            "3/2/2023",
            "4/2/2023",
            2,
            5,
            "",
            false,
            emptyList()
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
    fun `should get sort finance table record from database with society name`() {
        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityDao.findFinanceTableDataWithSocietyNameWithPageAngFilter(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("3/2/2023"),
                dateUtil.convertStringToLocalDateTime("4/2/2023"),
                1,
                10,
                "amount",
                true,
                emptyList()
            )
        } returns listOf<FinanceEntity>(
            FinanceEntity(1.0, "description 1", dateUtil.currentLocalDateTime),
            FinanceEntity(2.2, "description 2", dateUtil.currentLocalDateTime.plusDays(1)),
        )

        val financeTableData = financeService.getTableData(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            "3/2/2023",
            "4/2/2023",
            1,
            10,
            "amount",
            true,
            emptyList()
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
    fun `should get filtered finance table record from database with society name`() {
        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityDao.findFinanceTableDataWithSocietyNameWithPageAngFilter(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("3/2/2023"),
                dateUtil.convertStringToLocalDateTime("4/2/2023"),
                1,
                10,
                "",
                false,
                listOf("category 2")
            )
        } returns listOf<FinanceEntity>(
            FinanceEntity(1.0, "description 1", dateUtil.currentLocalDateTime, "category 2"),
            FinanceEntity(2.2, "description 2", dateUtil.currentLocalDateTime.plusDays(1), "category 2"),
        )

        val financeTableData = financeService.getTableData(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            "3/2/2023",
            "4/2/2023",
            1,
            10,
            "",
            false,
            listOf("category 2")
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
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
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

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
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
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

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
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
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

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
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
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

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
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
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

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
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
        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every { studentRepository.findByItsc(mockAuthRepository.validUserItsc) } returns Optional.of(studentEntity)

        val societyEntity: SocietyEntity = SocietyEntity(mockAuthRepository.testSocietyName)
        every { societyRepository.findByName(mockAuthRepository.testSocietyName) } returns Optional.of(societyEntity)

        val financeEntityList: List<FinanceEntity> = listOf<FinanceEntity>(
            FinanceEntity(12.3, "aaa", dateUtil.convertStringToLocalDateTime("2/7/2023"), "test category 1"),
            FinanceEntity(34.5, "bbb", dateUtil.convertStringToLocalDateTime("2/5/2023"), "test category 2")
        )

        val financeDto: CreateFinanceDto = CreateFinanceDto(
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
            financeService.createFinancialRecords(mockAuthRepository.validUserCookieToken, financeDto)

        assertEquals(financeEntityList.size, financeCreateRecord.size)
    }

    @Test
    fun `should not create finance record with itsc not joining the society`() {
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSocietyName

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.empty()

        val financeDto: CreateFinanceDto = CreateFinanceDto(
            "test society",
            listOf(
                FinanceRecordDto(12.3, "test category 1", "aaa", "2/7/2023"),
                FinanceRecordDto(34.5, "test category 2", "bbb", "2/5/2023"),
            )
        )

        val exception: AccessDeniedException = assertThrows {
            financeService.createFinancialRecords(mockAuthRepository.invalidUserCookieToken, financeDto)
        }

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
    }

    @Test
    fun `should not create finance record with invalid jwt token`() {
        val financeDto: CreateFinanceDto = CreateFinanceDto(
            "test society",
            listOf(
                FinanceRecordDto(12.3, "test category 1", "aaa", "2/7/2023"),
                FinanceRecordDto(34.5, "test category 2", "bbb", "2/5/2023"),
            )
        )

        val exception: Exception = assertThrows {
            financeService.createFinancialRecords("dummy", financeDto)
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should delete finance record from database`() {
        val deleteIdList: List<FinanceDeleteDto> = listOf(
            FinanceDeleteDto("4a487d9f-8f8d-4aec-b65b-22c4d28730c1"),
            FinanceDeleteDto("336d5d07-74a3-49b3-a6d3-30fa743fd490"),
        )

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityRepository.deleteAllById(deleteIdList.map { financeDeleteDto ->
                UUID.fromString(
                    financeDeleteDto.id
                )
            })
        } returns Unit

        val financeTableData = financeService.deleteFinanceRecords(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            deleteIdList
        )

        assertEquals(deleteIdList[0].id, financeTableData[0].id)
        assertEquals(deleteIdList[1].id, financeTableData[1].id)
    }

    @Test
    fun `should not delete finance record with itsc not joining the society`() {
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSocietyName

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.empty()

        val deleteIdList: List<FinanceDeleteDto> = listOf(
            FinanceDeleteDto("4a487d9f-8f8d-4aec-b65b-22c4d28730c1"),
            FinanceDeleteDto("336d5d07-74a3-49b3-a6d3-30fa743fd490"),
        )

        val exception: AccessDeniedException = assertThrows {
            financeService.deleteFinanceRecords(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                deleteIdList
            )
        }

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
    }

    @Test
    fun `should not delete finance record with invalid jwt token`() {
        val deleteIdList: List<FinanceDeleteDto> = listOf(
            FinanceDeleteDto("4a487d9f-8f8d-4aec-b65b-22c4d28730c1"),
            FinanceDeleteDto("336d5d07-74a3-49b3-a6d3-30fa743fd490"),
        )

        val exception: Exception = assertThrows {
            financeService.deleteFinanceRecords(
                "dummy",
                mockAuthRepository.testSocietyName,
                deleteIdList
            )
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should get total number of finance record from database within date range and filter`() {
        val financeRecordTotalNumberDto: FinanceRecordTotalNumberDto = FinanceRecordTotalNumberDto(200)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityDao.countTotalNumberOfFinanceRecordWithinDateRange(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("5/2/2023"),
                dateUtil.convertStringToLocalDateTime("6/2/2023"),
                listOf("category 1", "category 2")
            )
        } returns financeRecordTotalNumberDto

        val totalNumber = financeService.getFinanceRecordTotalNumber(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            "5/2/2023",
            "6/2/2023",
            listOf("category 1", "category 2")
        )

        assertEquals(200, totalNumber.total)
    }

    @Test
    fun `should not get total number of finance record from database within date range when itsc not joining the society`() {
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSocietyName

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.empty()

        val exception: AccessDeniedException = assertThrows {
            financeService.getFinanceRecordTotalNumber(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "5/2/2023",
                "6/2/2023"
            )
        }

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
    }

    @Test
    fun `should not get total number of finance record from database within date range when invalid jwt token`() {
        val exception: Exception = assertThrows {
            financeService.getFinanceRecordTotalNumber(
                "dummy",
                mockAuthRepository.testSocietyName,
                "5/2/2023",
                "6/2/2023"
            )
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }

    @Test
    fun `should get all category of finance record from database within date range`() {
        val categoryList: List<FinanceRecordCategoryDto> = listOf<FinanceRecordCategoryDto>(
            FinanceRecordCategoryDto("category a", "category a"),
            FinanceRecordCategoryDto("category b", "category b"),
        )

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            financeEntityRepository.getAllCategoryOfFinanceRecordWithinDateRange(
                mockAuthRepository.testSocietyName,
                dateUtil.convertStringToLocalDateTime("5/2/2023"),
                dateUtil.convertStringToLocalDateTime("6/2/2023")
            )
        } returns categoryList

        val totalNumber = financeService.getFinanceRecordCategory(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName,
            "5/2/2023",
            "6/2/2023"
        )

        assertEquals(categoryList[0].text, totalNumber[0].text)
        assertEquals(categoryList[0].value, totalNumber[0].value)
        assertEquals(categoryList[1].text, totalNumber[1].text)
        assertEquals(categoryList[1].value, totalNumber[1].value)
    }

    @Test
    fun `should not get all category of finance record from database within date range when itsc not joining the society`() {
        val itsc: String = mockAuthRepository.invalidUserItsc
        val societyName: String = mockAuthRepository.testSocietyName

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                itsc,
                societyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.empty()

        val exception: AccessDeniedException = assertThrows {
            financeService.getFinanceRecordTotalNumber(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "5/2/2023",
                "6/2/2023"
            )
        }

        assertEquals(
            "student with itsc: $itsc do not have ROLE_SOCIETY_MEMBER role of society: $societyName",
            exception.message
        )
    }

    @Test
    fun `should not get all category of finance record from database within date range when invalid jwt token`() {
        val exception: Exception = assertThrows {
            financeService.getFinanceRecordCategory(
                "dummy",
                mockAuthRepository.testSocietyName,
                "5/2/2023",
                "6/2/2023"
            )
        }

        assertEquals(MalformedJwtException::class, exception::class)
    }
}