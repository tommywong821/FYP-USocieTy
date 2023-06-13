package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.finance.model.*
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyEntityRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentEntityRepository
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import java.util.*

@Service
class FinanceService(
    private val financeEntityRepository: FinanceEntityRepository,
    private val financeEntityDao: FinanceEntityDao,
    private val studentRepository: StudentEntityRepository,
    private val societyRepository: SocietyEntityRepository,
    private val dateUtil: DateUtil,
    private val jwtUtil: JWTUtil,
) {

    fun getTableData(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String,
        pageIndex: Int,
        pageSize: Int,
        sortField: String,
        isAscend: Boolean,
        category: List<String>
    ): List<FinanceTableDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

        val financeEntityTableList: List<FinanceEntity>? =
            financeEntityDao.findFinanceTableDataWithSocietyNameWithPageAngFilter(
                societyName,
                dateUtil.convertStringToLocalDateTime(fromDateString),
                dateUtil.convertStringToLocalDateTime(toDateString),
                pageIndex,
                pageSize,
                sortField,
                isAscend,
                category
            )

        return financeEntityTableList?.map { financeEntity ->
            FinanceTableDto(
                financeEntity.uuid.toString(),
                dateUtil.convertLocalDateTimeToStringJS(financeEntity.date),
                financeEntity.amount,
                financeEntity.description,
                financeEntity.category,
                financeEntity.studentEntity.nickname
            )
        }
            ?: emptyList<FinanceTableDto>()
    }

    fun getTableData(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String,
    ): List<FinanceTableDto> {
        return getTableData(jwtToken, societyName, fromDateString, toDateString, 1, 10, "", false, emptyList())
    }

    fun getPieChartData(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String
    ): List<FinanceChartDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

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
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

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

        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

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
                dateUtil.convertLocalDateTimeToStringJS(financeEntity.date),
                financeEntity.amount,
                financeEntity.description,
                financeEntity.studentEntity.nickname
            )
        }
    }

    fun deleteFinanceRecords(
        jwtToken: String,
        societyName: String,
        deleteIdList: List<FinanceDeleteDto>
    ): List<FinanceDeleteDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

        financeEntityRepository.deleteAllById(deleteIdList.map { deleteId -> UUID.fromString(deleteId.id) })

        return deleteIdList
    }

    fun getFinanceRecordTotalNumber(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String
    ): FinanceRecordTotalNumberDto {
        return getFinanceRecordTotalNumber(jwtToken, societyName, fromDateString, toDateString, emptyList())
    }

    fun getFinanceRecordTotalNumber(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String,
        category: List<String>
    ): FinanceRecordTotalNumberDto {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

        return financeEntityDao.countTotalNumberOfFinanceRecordWithinDateRange(
            societyName,
            dateUtil.convertStringToLocalDateTime(
                fromDateString
            ), dateUtil.convertStringToLocalDateTime(toDateString),
            category
        )
    }

    fun getFinanceRecordCategory(
        jwtToken: String,
        societyName: String,
        fromDateString: String,
        toDateString: String
    ): List<FinanceRecordCategoryDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

        return financeEntityRepository.getAllCategoryOfFinanceRecordWithinDateRange(
            societyName,
            dateUtil.convertStringToLocalDateTime(
                fromDateString
            ), dateUtil.convertStringToLocalDateTime(toDateString)
        )
    }
}