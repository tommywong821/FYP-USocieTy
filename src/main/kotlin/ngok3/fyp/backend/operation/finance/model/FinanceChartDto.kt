package ngok3.fyp.backend.operation.finance.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "pieChart: name meaning category, barChart: name meaning month")
data class FinanceChartDto(
    val name: String,
    val value: Number
)
