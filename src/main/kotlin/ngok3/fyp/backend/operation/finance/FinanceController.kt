package ngok3.fyp.backend.operation.finance

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/finance")
class FinanceController(
    @Autowired val financeService: FinanceService
) {
    @Operation(summary = "get all finance record with society name returning in table dto")
    @Parameters(
        Parameter(
            name = "fromDate",
            description = "Date Format: dd-MM-yyyy \n\n eg: 02-03-2023 === 2nd February 2023"
        ), Parameter(name = "toDate", description = "Date Format: dd-MM-yyyy \n\n eg: 03-03-2023 === 3rd February 2023")
    )
    @GetMapping("/table")
    fun getFinanceTableData(
        @RequestParam("itsc") itsc: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceTableDto> {
        return financeService.getTableData(itsc, societyName, fromDateString, toDateString)
    }

    @GetMapping("/pieChart")
    fun getFinancePieChartData(
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceChartDto> {
        return financeService.getPieChartData(societyName, fromDateString, toDateString)
    }
}