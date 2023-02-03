package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import ngok3.fyp.backend.util.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FinanceService(
    @Autowired val financeEntityRepository: FinanceEntityRepository,
    @Autowired val dateUtil: DateUtil
) {
    fun getTableData(societyName: String, fromDateString: String, toDateString: String): List<FinanceTableDto> {

        val financeEntityTableList: List<FinanceEntity> = financeEntityRepository.findFinanceTableDataWithSocietyName(
            societyName,
            dateUtil.convertStringToLocalDateTime(fromDateString),
            dateUtil.convertStringToLocalDateTime(toDateString)
        )

        return financeEntityTableList.map { financeEntity ->
            FinanceTableDto(
                financeEntity.uuid.toString(),
                financeEntity.date?.let { dateUtil.convertLocalDateTimeToString(it) },
                financeEntity.amount,
                financeEntity.description,
                financeEntity.studentEntity?.nickname
            )
        }
    }

    fun getPieChartData(societyName: String, fromDateString: String, toDateString: String): List<FinanceChartDto> {
        return listOf<FinanceChartDto>()
    }
}