package ngok3.fyp.backend.operation.finance.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Date Format: M/d/yyyy eg: 2/3/2023 === 2nd February 2023")
data class FinanceRecordDto(
    val amount: Double? = 0.0,
    val description: String? = "",
    val date: String? = ""
)
