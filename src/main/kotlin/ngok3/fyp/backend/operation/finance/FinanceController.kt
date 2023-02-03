package ngok3.fyp.backend.operation.finance

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
    @GetMapping("/table")
    fun getFinanceTableData(
        @RequestParam("societyName") societyName: String,
        @RequestParam("fromDate") fromDateString: String,
        @RequestParam("toDate") toDateString: String,
    ): List<FinanceTableDto> {
        return financeService.getTableData(societyName, fromDateString, toDateString)
    }

}