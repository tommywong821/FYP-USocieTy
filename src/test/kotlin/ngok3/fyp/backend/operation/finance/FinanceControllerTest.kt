package ngok3.fyp.backend.operation.finance

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class FinanceControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var financeService: FinanceService

    @Test
    fun `should return finance record to table format`() {
        val tableData: List<FinanceTableDto> = listOf(
            FinanceTableDto(UUID.randomUUID().toString(), LocalDateTime.now().toString(), 1, "description 1", "user 1"),
            FinanceTableDto(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plusDays(1).toString(),
                2.2,
                "description 2",
                "user 2"
            )
        )

        every { financeService.getTableData() } returns tableData

        mockMvc.get("/finance/table") {
            headers {
                contentType = MediaType.APPLICATION_JSON
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
}