package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceRecordCategoryDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.*

interface FinanceEntityRepository : JpaRepository<FinanceEntity, UUID> {


    @Query("select new ngok3.fyp.backend.operation.finance.model.FinanceChartDto(f.category, cast(sum(f.amount) as double)) from FinanceEntity f where f.societyEntity.name = ?1 and f.date >= ?2 and f.date <= ?3 group by f.category")
    fun findFinancePieChartData(
        societyName: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime
    ): List<FinanceChartDto>

    @Query("select new ngok3.fyp.backend.operation.finance.model.FinanceChartDto(cast(to_char(f.date, 'Mon-yyyy') as text), cast(sum(f.amount) as double)) from FinanceEntity f where f.societyEntity.name = ?1 and f.date >= ?2 and f.date <= ?3 group by to_char(f.date, 'Mon-yyyy')")
    fun findFinanceBarChartData(
        societyName: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime
    ): List<FinanceChartDto>


    @Query("select new ngok3.fyp.backend.operation.finance.model.FinanceRecordCategoryDto(f.category, f.category) from FinanceEntity f where f.societyEntity.name = ?1 and f.date >= ?2 and f.date <= ?3 group by f.category")
    fun getAllCategoryOfFinanceRecordWithinDateRange(
        societyName: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime
    ): List<FinanceRecordCategoryDto>

}