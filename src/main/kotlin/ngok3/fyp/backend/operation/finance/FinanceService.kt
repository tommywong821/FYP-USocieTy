package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import org.springframework.stereotype.Service

@Service
class FinanceService {
    fun getTableData(): List<FinanceTableDto> {
        return emptyList()
    }
}