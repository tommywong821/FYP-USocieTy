package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import ngok3.fyp.backend.util.DateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class FinanceService(
    @Autowired val financeEntityRepository: FinanceEntityRepository,
    @Autowired val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository,
    @Autowired val dateUtil: DateUtil
) {
    fun getTableData(
        itsc: String,
        societyName: String,
        fromDateString: String,
        toDateString: String
    ): List<FinanceTableDto> {

        if (enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                itsc,
                societyName,
                EnrolledStatus.SUCCESS
            ).isEmpty
        ) {
            throw AccessDeniedException("student with itsc: ${itsc} do not belong to this society: ${societyName}")
        }

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

        return financeEntityRepository.findFinanceChartData(
            societyName,
            dateUtil.convertStringToLocalDateTime(fromDateString),
            dateUtil.convertStringToLocalDateTime(toDateString)
        )
    }
}