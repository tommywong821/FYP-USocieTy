package ngok3.fyp.backend.operation.finance.model

data class FinanceTableDto(
    val id: String = "",
    val date: String = "",
    val amount: Number = 0.0,
    val description: String = "",
    val category: String = "",
    val editBy: String = ""
)
