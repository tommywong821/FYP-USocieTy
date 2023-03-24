package ngok3.fyp.backend.operation.enrolled.society_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class EnrolledSocietyRecordService(
    private val enrolledSocietyRepository: EnrolledSocietyRecordRepository,
    private val jwtUtil: JWTUtil
) {
    fun updateEnrolledSocietyRecord(jwtToken: String, updateEnrolledSocietyRecordDto: UpdateEnrolledSocietyRecordDto) {
        val enrolledSocietyRecordEntity: EnrolledSocietyRecordEntity = enrolledSocietyRepository.findById(
            EnrolledSocietyRecordKey(
                UUID.fromString(updateEnrolledSocietyRecordDto.studentId),
                UUID.fromString(updateEnrolledSocietyRecordDto.societyId)
            )
        ).orElseThrow {
            Exception("Enrolled Event Record with student id: ${updateEnrolledSocietyRecordDto.studentId} and society id: ${updateEnrolledSocietyRecordDto.societyId} is not found")
        }

        jwtUtil.verifyUserMemberRoleOfSociety(
            jwtToken = jwtToken,
            enrolledSocietyRecordEntity.societyEntity.name
        )

        enrolledSocietyRecordEntity.status = updateEnrolledSocietyRecordDto.status

        enrolledSocietyRepository.save(enrolledSocietyRecordEntity)
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