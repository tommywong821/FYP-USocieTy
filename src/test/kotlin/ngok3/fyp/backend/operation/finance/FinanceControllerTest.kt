package ngok3.fyp.backend.operation.finance

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.util.LinkedMultiValueMap
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FinanceControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    @Autowired val dateUtil: DateUtil
) {
    @MockkBean
    lateinit var financeService: FinanceService

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

        every { financeService.getTableData("test society", "03-02-2023", "04-02-2023") } returns tableData

        mockMvc.get("/finance/table") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", "test society")
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
    fun `should return finance record to pie chart format`() {
        val pieChartData: List<FinanceChartDto> = listOf(
            FinanceChartDto("Souvenir", 5740),
            FinanceChartDto("Supplies", 4736)
        )

        every { financeService.getPieChartData("test society", "03-02-2023", "04-02-2023") } returns pieChartData

        mockMvc.get("/finance/pieChart") {
            headers {
                contentType = MediaType.APPLICATION_JSON
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", "test society")
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
}