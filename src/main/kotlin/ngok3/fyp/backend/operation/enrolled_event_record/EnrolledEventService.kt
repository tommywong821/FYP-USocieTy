package ngok3.fyp.backend.operation.enrolled_event_record

import ngok3.fyp.backend.util.JWTUtil
import org.springframework.stereotype.Service
import java.util.*

@Service
class EnrolledEventService(
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val jwtUtil: JWTUtil
) {
    fun updateEnrolledEventService(jwtToken: String, updateEnrolledEventRecordDto: UpdateEnrolledEventRecordDto) {
        val enrolledEventRecordEntity: EnrolledEventRecordEntity = enrolledEventRecordRepository.findById(
            EnrolledEventRecordKey(
                UUID.fromString(updateEnrolledEventRecordDto.studentId),
                UUID.fromString(updateEnrolledEventRecordDto.eventId)
            )
        ).orElseThrow {
            Exception("Enrolled Event Record with student id: ${updateEnrolledEventRecordDto.studentId} and event id: ${updateEnrolledEventRecordDto.eventId} is not found")
        }

        jwtUtil.verifyUserEnrolledSociety(
            jwtToken = jwtToken,
            enrolledEventRecordEntity.eventEntity.societyEntity.name
        )

        enrolledEventRecordEntity.status = updateEnrolledEventRecordDto.status

        enrolledEventRecordRepository.save(enrolledEventRecordEntity)
    }

}