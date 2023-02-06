package ngok3.fyp.backend.operation.finance.model

data class CreateFinanceDto(
    val societyName: String? = "",
    val financeRecords: List<FinanceRecordDto>? = emptyList()
)
