package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FinanceService(
    @Autowired val financeEntityRepository: FinanceEntityRepository,
    @Autowired val dateUtil: DateUtil,
    @Autowired val jwtUtil: JWTUtil,
) {
    fun getTableData(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String
    ): List<FinanceTableDto> {
        jwtUtil.verifyUserEnrolledSociety(jwtToken, societyName)

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

    fun getPieChartData(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String
    ): List<FinanceChartDto> {
        jwtUtil.verifyUserEnrolledSociety(jwtToken, societyName)

        return financeEntityRepository.findFinanceChartData(
            societyName,
            dateUtil.convertStringToLocalDateTime(fromDateString),
            dateUtil.convertStringToLocalDateTime(toDateString)
        )
    }
}