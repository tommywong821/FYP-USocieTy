package ngok3.fyp.backend.operation.finance

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.responses.ApiResponse
import ngok3.fyp.backend.operation.finance.model.*
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

    @Operation(summary = "delete finance record(s) with user input form")
    @DeleteMapping
    fun deleteFinancialRecords(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("id") deleteIdList: List<FinanceDeleteDto>
    ): List<FinanceDeleteDto> {
        return financeService.deleteFinanceRecords(jwtToken, societyName, deleteIdList)
    }

    @Operation(summary = "get total number of finance record within date range")
    @Parameters(
        Parameter(
            name = "fromDate",
            description = "Date Format: M/d/yyyy \n\n eg: 2/3/2023 === 2nd February 2023"
        ), Parameter(name = "toDate", description = "Date Format: M/d/yyyy \n\n eg: 3/3/2023 === 3rd February 2023")
    )
    @GetMapping("/totalNumber")
    fun getFinanceRecordTotalNumber(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): FinanceRecordTotalNumberDto {
        return financeService.getFinanceRecordTotalNumber(jwtToken, societyName, fromDateString, toDateString)
    }

    @Operation(summary = "get all category of finance record within date range")
    @Parameters(
        Parameter(
            name = "fromDate",
            description = "Date Format: M/d/yyyy \n\n eg: 2/3/2023 === 2nd February 2023"
        ), Parameter(name = "toDate", description = "Date Format: M/d/yyyy \n\n eg: 3/3/2023 === 3rd February 2023")
    )
    @GetMapping("/category")
    fun getFinanceRecordCategory(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceRecordCategoryDto> {
        return financeService.getFinanceRecordCategory(jwtToken, societyName, fromDateString, toDateString)
    }
}