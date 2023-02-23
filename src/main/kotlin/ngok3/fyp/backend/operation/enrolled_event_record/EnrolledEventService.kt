package ngok3.fyp.backend.operation.enrolled_event_record

import ngok3.fyp.backend.util.JWTUtil
import org.springframework.stereotype.Service
import java.time.LocalDateTime
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

    fun getAllEnrolledEvent(itsc: String, pageNum: Int, pageSize: Int): List<EnrolledEventDto> {
        return enrolledEventRecordRepository.findByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqualOrderByEventEntity_StartDateAsc(
            itsc,
            LocalDateTime.now()
        ).map { enrolledEventEntity -> EnrolledEventDto(enrolledEventEntity) }
    }

    fun countEnrolledEvent(itsc: String): Long {
        return enrolledEventRecordRepository.countByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqual(
            itsc,
            LocalDateTime.now()
        )
    }
}