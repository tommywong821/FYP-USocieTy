package ngok3.fyp.backend.operation.finance

import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FinanceServiceTest(
    @Autowired private val dateUtil: DateUtil
) {
    private val financeEntityRepository: FinanceEntityRepository = mockk(relaxed = true)
    private val financeService: FinanceService = FinanceService(financeEntityRepository, dateUtil)

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

        val financeTableData = financeService.getTableData("test society", "03-02-2023", "04-02-2023")

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