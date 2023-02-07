package ngok3.fyp.backend.operation.finance

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import ngok3.fyp.backend.operation.finance.model.CreateFinanceDto
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/finance")
class FinanceController(
    @Autowired val financeService: FinanceService
) {
    @Operation(summary = "get all finance record with society name returning in table dto")
    @Parameters(
        Parameter(
            name = "fromDate",
            description = "Date Format: M/d/yyyy \n\n eg: 2/3/2023 === 2nd February 2023"
        ), Parameter(name = "toDate", description = "Date Format: M/d/yyyy \n\n eg: 3/3/2023 === 3rd February 2023")
    )
    @GetMapping("/table")
    fun getFinanceTableData(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceTableDto> {
        return financeService.getTableData(jwtToken, societyName, fromDateString, toDateString)
    }

    @Operation(summary = "get all finance record with society name group by category")
    @Parameters(
        Parameter(
            name = "fromDate",
            description = "Date Format: M/d/yyyy \n\n eg: 2/3/2023 === 2nd February 2023"
        ), Parameter(name = "toDate", description = "Date Format: M/d/yyyy \n\n eg: 3/3/2023 === 3rd February 2023")
    )
    @ApiResponse(description = "FinanceChartDto: name === category meaning")
    @GetMapping("/pieChart")
    fun getFinancePieChartData(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceChartDto> {
        return financeService.getPieChartData(jwtToken, societyName, fromDateString, toDateString)
    }

    @Operation(summary = "get all finance record with society name group by month and year")
    @Parameters(
        Parameter(
            name = "fromDate",
            description = "Date Format: M/d/yyyy \n\n eg: 2/3/2023 === 2nd February 2023"
        ), Parameter(name = "toDate", description = "Date Format: M/d/yyyy \n\n eg: 3/3/2023 === 3rd February 2023")
    )
    @ApiResponse(description = "FinanceChartDto: name === month meaning")
    @GetMapping("/barChart")
    fun getFinanceBarChartData(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceChartDto> {
        return financeService.getBarChartData(jwtToken, societyName, fromDateString, toDateString)
    }

    @Operation(summary = "create finance record(s) with user input form")
    @PostMapping
    fun createFinancialRecords(
        @CookieValue("token") jwtToken: String,
        @RequestBody createFinanceDto: CreateFinanceDto
    ): List<FinanceTableDto> {
        return financeService.createFinancialRecords(jwtToken, createFinanceDto)
    }
}