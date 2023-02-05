package ngok3.fyp.backend.operation.finance;

import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.*

interface FinanceEntityRepository : JpaRepository<FinanceEntity, UUID> {

    @Query("select new ngok3.fyp.backend.operation.finance.model.FinanceChartDto(f.category, count(f.amount)) from FinanceEntity f where f.societyEntity.name = ?1 and f.date >= ?2 and f.date <= ?3 group by f.category")
    fun findFinanceChartData(
        name: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime
    ): List<FinanceChartDto>


    @Query(
        """select f from FinanceEntity f where f.societyEntity.name = ?1 and f.date >= ?2 and f.date <= ?3 order by f.date"""
    )
    fun findFinanceTableDataWithSocietyName(
        name: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime
    ): List<FinanceEntity>

}