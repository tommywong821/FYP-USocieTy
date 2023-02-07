package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.finance.model.CreateFinanceDto
import ngok3.fyp.backend.operation.finance.model.FinanceChartDto
import ngok3.fyp.backend.operation.finance.model.FinanceTableDto
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class FinanceService(
    @Autowired val financeEntityRepository: FinanceEntityRepository,
    @Autowired val studentRepository: StudentRepository,
    @Autowired val societyRepository: SocietyRepository,
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
                financeEntity.category,
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

        return financeEntityRepository.findFinancePieChartData(
            societyName,
            dateUtil.convertStringToLocalDateTime(fromDateString),
            dateUtil.convertStringToLocalDateTime(toDateString)
        )
    }

    fun getBarChartData(
        jwtToken: String,
        societyName: String,
        fromDate: String,
        toDate: String
    ): List<FinanceChartDto> {
        jwtUtil.verifyUserEnrolledSociety(jwtToken, societyName)

        return financeEntityRepository.findFinanceBarChartData(
            societyName,
            dateUtil.convertStringToLocalDateTime(fromDate),
            dateUtil.convertStringToLocalDateTime(toDate)
        )
    }

    fun createFinancialRecords(jwtToken: String, createFinanceDto: CreateFinanceDto): List<FinanceTableDto> {
        val societyName: String = createFinanceDto.societyName

        if (StringUtils.isBlank(createFinanceDto.societyName)) {
            throw Exception("Society name can not be null")
        }

        jwtUtil.verifyUserEnrolledSociety(jwtToken, societyName)

        val itsc: String = jwtUtil.getClaimFromJWTToken(jwtToken, "itsc")

        val studentOpt: Optional<StudentEntity> = studentRepository.findByItsc(itsc)
        if (studentOpt.isEmpty) {
            throw Exception("Student with $itsc is not exist")
        }
        val student: StudentEntity = studentOpt.get()

        val societyOpt: Optional<SocietyEntity> = societyRepository.findByName(societyName)
        if (societyOpt.isEmpty) {
            throw Exception("Society with $societyName is not exist")
        }
        val society: SocietyEntity = societyOpt.get()

        val financeEntityList: ArrayList<FinanceEntity> = ArrayList<FinanceEntity>()

        for (financeDto in createFinanceDto.financeRecords) {
            val financeEntity: FinanceEntity = FinanceEntity(
                financeDto.amount,
                financeDto.description,
                dateUtil.convertStringToLocalDateTime(financeDto.date),
                financeDto.category
            )
            financeEntity.studentEntity = student
            financeEntity.societyEntity = society

            financeEntityList.add(financeEntity)
        }

        return financeEntityRepository.saveAll(financeEntityList).map { financeEntity ->
            FinanceTableDto(
                financeEntity.uuid.toString(),
                financeEntity.date?.let { dateUtil.convertLocalDateTimeToString(it) },
                financeEntity.amount,
                financeEntity.description,
                financeEntity.studentEntity?.nickname
            )
        }
    }

}