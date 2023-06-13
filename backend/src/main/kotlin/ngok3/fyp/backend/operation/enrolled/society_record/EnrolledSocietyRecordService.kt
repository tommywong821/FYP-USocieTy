package ngok3.fyp.backend.operation.enrolled.society_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.model.StudentEnrolledSocietyRecordDto
import ngok3.fyp.backend.operation.enrolled.society_record.model.UpdateEnrolledSocietyRecordDto
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class EnrolledSocietyRecordService(
    private val enrolledSocietyRepository: EnrolledSocietyRecordEntityRepository,
    private val jwtUtil: JWTUtil
) {
    fun updateEnrolledSocietyRecord(
        jwtToken: String,
        updateEnrolledSocietyRecordDtoList: List<UpdateEnrolledSocietyRecordDto>
    ) {
        val enrolledSocietyRecordEntityMap = enrolledSocietyRepository.findAllById(
            updateEnrolledSocietyRecordDtoList.map { updateEnrolledSocietyRecordDto: UpdateEnrolledSocietyRecordDto ->
                EnrolledSocietyRecordKey(
                    UUID.fromString(updateEnrolledSocietyRecordDto.studentId),
                    UUID.fromString(updateEnrolledSocietyRecordDto.societyId)
                )
            }
        ).toList().associateBy({ it.id }, { it }).toMutableMap()

        if (enrolledSocietyRecordEntityMap.isEmpty()) {
            throw Exception("updateEnrolledSocietyRecordDtoList is not exist")
        }
        jwtUtil.verifyUserMemberRoleOfSociety(
            jwtToken = jwtToken,
            enrolledSocietyRecordEntityMap.values.first().societyEntity.name
        )

        updateEnrolledSocietyRecordDtoList.forEach { updateEnrolledSocietyRecordDto: UpdateEnrolledSocietyRecordDto ->
            run {
                val key: EnrolledSocietyRecordKey = EnrolledSocietyRecordKey(
                    studentUuid = UUID.fromString(updateEnrolledSocietyRecordDto.studentId),
                    societyUuid = UUID.fromString(updateEnrolledSocietyRecordDto.societyId)
                )
                if (enrolledSocietyRecordEntityMap.containsKey(key)) {
                    enrolledSocietyRecordEntityMap[key]?.status = updateEnrolledSocietyRecordDto.status
                }
            }
        }

        enrolledSocietyRepository.saveAll(enrolledSocietyRecordEntityMap.values.toList())
    }

    fun getEnrolledSocietyRecord(jwtToken: String, societyName: String): List<StudentEnrolledSocietyRecordDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(
            jwtToken = jwtToken,
            societyName
        )
        return enrolledSocietyRepository.findBySocietyEntity_NameAndStatusNotEqual(societyName, EnrolledStatus.SUCCESS)
            .map { enrolledSocietyRecordEntity ->
                StudentEnrolledSocietyRecordDto(
                    studentId = enrolledSocietyRecordEntity.studentEntity.uuid.toString(),
                    societyId = enrolledSocietyRecordEntity.societyEntity.uuid.toString(),
                    itsc = enrolledSocietyRecordEntity.studentEntity.itsc,
                    name = enrolledSocietyRecordEntity.studentEntity.nickname,
                    status = enrolledSocietyRecordEntity.status
                )
            }
    }
}