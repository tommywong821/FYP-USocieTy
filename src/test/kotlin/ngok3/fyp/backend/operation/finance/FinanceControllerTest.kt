package ngok3.fyp.backend.operation.finance

import com.ninjasquad.springmockk.MockkBean
import io.jsonwebtoken.MalformedJwtException
import io.mockk.every
import io.mockk.mockkStatic
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.finance.model.CreateFinanceDto
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceRecordDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.util.LinkedMultiValueMap
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.servlet.http.Cookie


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FinanceControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    @Autowired val dateUtil: DateUtil
) {
    @MockkBean
    lateinit var financeService: FinanceService

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    companion object {
        @BeforeAll
        @JvmStatic
        fun setUp() {
            val clock: Clock = Clock.fixed(Instant.parse("2014-12-22T10:15:30.00Z"), ZoneId.of("UTC"))
            val dateTime: LocalDateTime = LocalDateTime.now(clock)
            mockkStatic(LocalDateTime::class)
            every { LocalDateTime.now() } returns dateTime
        }
    }


    @Test
    fun `should return finance record to table format`() {
        val tableData: List<FinanceTableDto> = listOf(
            FinanceTableDto(
                UUID.randomUUID().toString(),
                dateUtil.currentLocalDateTime.toString(),
                1,
                "description 1",
                "user 1"
            ),
            FinanceTableDto(
                UUID.randomUUID().toString(),
                dateUtil.currentLocalDateTime.plusDays(1).toString(),
                2.2,
                "description 2",
                "user 2"
            )
        )

        every {
            financeService.getTableData(
                mockAuthRepository.validUserCookieToken,
                mockAuthRepository.testSocietyName,
                "03-02-2023",
                "04-02-2023"
            )
        } returns tableData

        mockMvc.get("/finance/table") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(tableData.size)
            }
            jsonPath("$[*].id") {
                value(tableData.map { financeTableDto -> financeTableDto.id })
            }
            jsonPath("$[*].date") {
                value(tableData.map { financeTableDto -> financeTableDto.date })
            }
            jsonPath("$[*].amount") {
                value(tableData.map { financeTableDto -> financeTableDto.amount })
            }
            jsonPath("$[*].description") {
                value(tableData.map { financeTableDto -> financeTableDto.description })
            }
            jsonPath("$[*].editBy") {
                value(tableData.map { financeTableDto -> financeTableDto.editBy })
            }
        }
    }

    @Test
    fun `should return 401 error when user no belong to that society and try to get finance record to table format`() {
        val societyName: String = mockAuthRepository.testSocietyName
        val itsc: String = mockAuthRepository.invalidUserItsc
        every {
            financeService.getTableData(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        } throws AccessDeniedException("student with itsc: ${itsc} do not belong to this society: ${societyName}")

        mockMvc.get("/finance/table") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.invalidUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Unauthorized Access: student with itsc: ${itsc} do not belong to this society: ${societyName}")
            }
        }
    }

    @Test
    fun `should return 401 error when invalid cookie token to get finance table data`() {
        val societyName: String = mockAuthRepository.testSocietyName
        every {
            financeService.getTableData(
                "dummy",
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        } throws MalformedJwtException("Invalid JWT token")

        mockMvc.get("/finance/table") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", "dummy"))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Invalid JWT token")
            }
        }
    }

    @Test
    fun `should return finance record to pie chart format`() {
        val pieChartData: List<FinanceChartDto> = listOf(
            FinanceChartDto("Souvenir", 5740.0),
            FinanceChartDto("Supplies", 4736.0)
        )

        every {
            financeService.getPieChartData(
                mockAuthRepository.validUserCookieToken,
                mockAuthRepository.testSocietyName,
                "03-02-2023",
                "04-02-2023"
            )
        } returns pieChartData

        mockMvc.get("/finance/pieChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(pieChartData.size)
            }
            jsonPath("$[*].name") {
                value(pieChartData.map { financeChartDto -> financeChartDto.name })
            }
            jsonPath("$[*].value") {
                value(pieChartData.map { financeChartDto -> financeChartDto.value })
            }
        }
    }

    @Test
    fun `should return 401 error when user no belong to that society and try to get finance record to pie chart format`() {
        val societyName: String = mockAuthRepository.testSocietyName
        val itsc: String = mockAuthRepository.invalidUserItsc
        every {
            financeService.getPieChartData(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        } throws AccessDeniedException("student with itsc: ${itsc} do not belong to this society: ${societyName}")

        mockMvc.get("/finance/pieChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.invalidUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Unauthorized Access: student with itsc: ${itsc} do not belong to this society: ${societyName}")
            }
        }
    }

    @Test
    fun `should return 401 error when invalid cookie token to get finance pie chart data`() {
        val societyName: String = mockAuthRepository.testSocietyName
        every {
            financeService.getPieChartData(
                "dummy",
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        } throws MalformedJwtException("Invalid JWT token")

        mockMvc.get("/finance/pieChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", "dummy"))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Invalid JWT token")
            }
        }
    }

    @Test
    fun `should return finance record to bar chart format`() {
        val barChartData: List<FinanceChartDto> = listOf(
            FinanceChartDto("Jan-2023", 7187.0),
            FinanceChartDto("Feb-2023", 8738.0)
        )

        every {
            financeService.getBarChartData(
                mockAuthRepository.validUserCookieToken,
                mockAuthRepository.testSocietyName,
                "03-02-2023",
                "04-02-2023"
            )
        } returns barChartData

        mockMvc.get("/finance/barChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(barChartData.size)
            }
            jsonPath("$[*].name") {
                value(barChartData.map { financeChartDto -> financeChartDto.name })
            }
            jsonPath("$[*].value") {
                value(barChartData.map { financeChartDto -> financeChartDto.value })
            }
        }
    }

    @Test
    fun `should return 401 error when user no belong to that society and try to get finance record to bar chart format`() {
        val societyName: String = mockAuthRepository.testSocietyName
        val itsc: String = mockAuthRepository.invalidUserItsc
        every {
            financeService.getBarChartData(
                mockAuthRepository.invalidUserCookieToken,
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        } throws AccessDeniedException("student with itsc: ${itsc} do not belong to this society: ${societyName}")

        mockMvc.get("/finance/barChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.invalidUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Unauthorized Access: student with itsc: ${itsc} do not belong to this society: ${societyName}")
            }
        }
    }

    @Test
    fun `should return 401 error when invalid cookie token to get finance bar chart data`() {
        val societyName: String = mockAuthRepository.testSocietyName
        every {
            financeService.getBarChartData(
                "dummy",
                societyName,
                "03-02-2023",
                "04-02-2023"
            )
        } throws MalformedJwtException("Invalid JWT token")

        mockMvc.get("/finance/barChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", "dummy"))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("fromDate", "03-02-2023")
                add("toDate", "04-02-2023")
            }
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Invalid JWT token")
            }
        }
    }

    @Test
    fun `should create financial records`() {
        val societyEntity: SocietyEntity = SocietyEntity(mockAuthRepository.testSocietyName)
        val studentEntity: StudentEntity = StudentEntity(
            mockAuthRepository.validUserItsc,
            mockAuthRepository.validUserNickname,
            mockAuthRepository.validUserMail
        )

        val financeEntityList: List<FinanceEntity> = listOf<FinanceEntity>(
            FinanceEntity(1.0, "test description 1", LocalDateTime.now(), "test category 1"),
            FinanceEntity(2.2, "test description 2", LocalDateTime.now().plusDays(1), "test category 2")
        )

        for (financeEntity in financeEntityList) {
            financeEntity.societyEntity = societyEntity
            financeEntity.studentEntity = studentEntity
        }

        val createFinanceDto: CreateFinanceDto = CreateFinanceDto(
            mockAuthRepository.testSocietyName,
            listOf(
                FinanceRecordDto(123.0, "test category 1", "test description 1", "2/7/2023"),
                FinanceRecordDto(345.0, "test category 2", "test description 2", "2/8/2023"),
            )
        )

        every {
            financeService.createFinancialRecords(
                mockAuthRepository.validUserCookieToken,
                createFinanceDto
            )
        } returns financeEntityList

        mockMvc.post("/finance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            content =
                "{\"societyName\":\"test society\",\"financeRecords\":[{\"amount\":123.0,\"category\":\"test category 1\",\"description\":\"test description 1\",\"date\":\"2\\/7\\/2023\"},{\"amount\":345.0,\"category\":\"test category 2\",\"description\":\"test description 2\",\"date\":\"2\\/8\\/2023\"}]}"
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(financeEntityList.size)
            }
            jsonPath("$[*].amount") {
                value(financeEntityList.map { financeEntity -> financeEntity.amount })
            }
            jsonPath("$[*].description") {
                value(financeEntityList.map { financeEntity -> financeEntity.description })
            }
            jsonPath("$[*].date") {
                value(financeEntityList.map { financeEntity -> financeEntity.date.toString() })
            }
            jsonPath("$[*].category") {
                value(financeEntityList.map { financeEntity -> financeEntity.category })
            }
        }
    }

    @Test
    fun `should return 401 error when user no belong to that society and try to create finance record`() {
        val societyName: String = mockAuthRepository.testSocietyName
        val itsc: String = mockAuthRepository.invalidUserItsc
        val createFinanceDto: CreateFinanceDto = CreateFinanceDto(
            societyName,
            listOf(
                FinanceRecordDto(123.0, "test category 1", "test description 1", "2/7/2023"),
                FinanceRecordDto(345.0, "test category 2", "test description 2", "2/8/2023"),
            )
        )

        every {
            financeService.createFinancialRecords(mockAuthRepository.invalidUserCookieToken, createFinanceDto)
        } throws AccessDeniedException("student with itsc: ${itsc} do not belong to this society: ${societyName}")

        mockMvc.post("/finance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.invalidUserCookieToken))
            }
            content =
                "{\"societyName\":\"test society\",\"financeRecords\":[{\"amount\":123.0,\"category\":\"test category 1\",\"description\":\"test description 1\",\"date\":\"2\\/7\\/2023\"},{\"amount\":345.0,\"category\":\"test category 2\",\"description\":\"test description 2\",\"date\":\"2\\/8\\/2023\"}]}"
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Unauthorized Access: student with itsc: ${itsc} do not belong to this society: ${societyName}")
            }
        }
    }

    @Test
    fun `should return 401 error when invalid cookie token try to create finance record`() {
        val societyName: String = mockAuthRepository.testSocietyName
        val createFinanceDto: CreateFinanceDto = CreateFinanceDto(
            mockAuthRepository.testSocietyName,
            listOf(
                FinanceRecordDto(123.0, "test category 1", "test description 1", "2/7/2023"),
                FinanceRecordDto(345.0, "test category 2", "test description 2", "2/8/2023"),
            )
        )
        every {
            financeService.createFinancialRecords("dummy", createFinanceDto)
        } throws MalformedJwtException("Invalid JWT token")

        mockMvc.post("/finance") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", "dummy"))
            }
            content =
                "{\"societyName\":\"test society\",\"financeRecords\":[{\"amount\":123.0,\"category\":\"test category 1\",\"description\":\"test description 1\",\"date\":\"2\\/7\\/2023\"},{\"amount\":345.0,\"category\":\"test category 2\",\"description\":\"test description 2\",\"date\":\"2\\/8\\/2023\"}]}"
        }.andDo { print() }.andExpect {
            status { isUnauthorized() }
            jsonPath("$.status") {
                value(401)
            }
            jsonPath("$.message") {
                value("Invalid JWT token")
            }
        }
    }
}