package ngok3.fyp.backend.operation.enrolled.event_record

import ngok3.fyp.backend.operation.enrolled.event_record.model.EnrolledEventDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.StudentEnrolledEventRecordDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.UpdateEnrolledEventRecordDto
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class EnrolledEventRecordService(
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val jwtUtil: JWTUtil
) {
    fun updateEnrolledEventRecord(
        jwtToken: String,
        updateEnrolledEventRecordDtoList: List<UpdateEnrolledEventRecordDto>
    ) {
        val enrolledEventRecordEntityMap = enrolledEventRecordRepository.findAllById(
            updateEnrolledEventRecordDtoList.map { updateEnrolledEventRecordDto ->
                EnrolledEventRecordKey(
                    UUID.fromString(updateEnrolledEventRecordDto.studentId),
                    UUID.fromString(updateEnrolledEventRecordDto.eventId)
                )
            }
        ).toList().associateBy({ it.id }, { it }).toMutableMap()


        jwtUtil.verifyUserAdminRoleOfSociety(
            jwtToken = jwtToken,
            enrolledEventRecordEntityMap.values.first().eventEntity.societyEntity.name
        )

        updateEnrolledEventRecordDtoList.forEach { updateEnrolledEventRecordDto: UpdateEnrolledEventRecordDto ->
            run {
                val key: EnrolledEventRecordKey = EnrolledEventRecordKey(
                    studentUuid = UUID.fromString(updateEnrolledEventRecordDto.studentId),
                    eventUuid = UUID.fromString(updateEnrolledEventRecordDto.eventId)
                )
                if (enrolledEventRecordEntityMap.containsKey(key)) {
                    enrolledEventRecordEntityMap[key]?.status = updateEnrolledEventRecordDto.status
                }
            }
        }

        enrolledEventRecordRepository.saveAll(enrolledEventRecordEntityMap.values.toList())
    }

    fun getAllEnrolledEvent(itsc: String, pageNum: Int, pageSize: Int): List<EnrolledEventDto> {
        return enrolledEventRecordRepository.findByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqualOrderByEventEntity_StartDateAsc(
            itsc,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        ).map { enrolledEventEntity -> EnrolledEventDto(enrolledEventEntity) }
    }

    fun countEnrolledEvent(itsc: String): Long {
        return enrolledEventRecordRepository.countByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqual(
            itsc,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        )
    }

    fun getStudentEnrolledEventRecord(
        eventUuid: String,
        pageNum: Int,
        pageSize: Int
    ): List<StudentEnrolledEventRecordDto> {
        TODO("Not yet implemented")
        return emptyList<StudentEnrolledEventRecordDto>()
    }
}