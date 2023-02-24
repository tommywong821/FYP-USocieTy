package ngok3.fyp.backend.operation.enrolled.society_record

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

        jwtUtil.verifyUserEnrolledSociety(
            jwtToken = jwtToken,
            enrolledSocietyRecordEntity.societyEntity.name
        )

        enrolledSocietyRecordEntity.status = updateEnrolledSocietyRecordDto.status

        enrolledSocietyRepository.save(enrolledSocietyRecordEntity)
    }
}