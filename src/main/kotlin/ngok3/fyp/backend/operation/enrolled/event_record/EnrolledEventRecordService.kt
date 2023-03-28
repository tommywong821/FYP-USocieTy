package ngok3.fyp.backend.operation.enrolled.event_record

import ngok3.fyp.backend.operation.TotalCountDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.EnrolledEventDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.StudentEnrolledEventRecordDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.UpdateEnrolledEventRecordDto
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class EnrolledEventRecordService(
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val eventRepository: EventRepository,
    private val jwtUtil: JWTUtil,
    private val dateUtil: DateUtil
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


        jwtUtil.verifyUserMemberRoleOfSociety(
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
                    enrolledEventRecordEntityMap[key]?.enrollStatus = updateEnrolledEventRecordDto.enrolledStatus
                    enrolledEventRecordEntityMap[key]?.paymentStatus = updateEnrolledEventRecordDto.paymentStatus
                }
            }
        }

        enrolledEventRecordRepository.saveAll(enrolledEventRecordEntityMap.values.toList())
    }

    fun getAllEnrolledEvent(itsc: String, pageNum: Int, pageSize: Int): List<EnrolledEventDto> {
        return enrolledEventRecordRepository.findByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqualOrderByEventEntity_StartDateAsc(
            itsc,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        ).map { enrolledEventEntity ->
            EnrolledEventDto(
                name = enrolledEventEntity.eventEntity.name,
                location = enrolledEventEntity.eventEntity.location,
                startDate = dateUtil.convertLocalDateTimeToString(enrolledEventEntity.eventEntity.startDate),
                endDate = dateUtil.convertLocalDateTimeToString(enrolledEventEntity.eventEntity.endDate),
                category = enrolledEventEntity.eventEntity.category,
                enrolledStatus = enrolledEventEntity.enrollStatus,
                paymentStatus = enrolledEventEntity.paymentStatus
            )
        }
    }

    fun countEnrolledEvent(itsc: String): Long {
        return enrolledEventRecordRepository.countByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqual(
            itsc,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        )
    }

    fun getStudentEnrolledEventRecord(
        jwtToken: String,
        eventUuid: String,
        pageNum: Int,
        pageSize: Int
    ): List<StudentEnrolledEventRecordDto> {
        val eventUuidObj: UUID = UUID.fromString(eventUuid)
        val eventEntity: EventEntity = eventRepository.findById(eventUuidObj).orElseThrow {
            Exception("Event with id: $eventUuid is not found")
        }

        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken = jwtToken, societyName = eventEntity.societyEntity.name)

        return enrolledEventRecordRepository.findAllEnrolledRecordStudentByEventIdWithPaging(
            eventUuidObj,
            PageRequest.of(pageNum, pageSize)
        ).map { enrolledEventRecordEntity: EnrolledEventRecordEntity ->
            StudentEnrolledEventRecordDto(
                studentId = enrolledEventRecordEntity.studentEntity.uuid.toString(),
                itsc = enrolledEventRecordEntity.studentEntity.itsc,
                paymentStatus = enrolledEventRecordEntity.paymentStatus,
                enrolledStatus = enrolledEventRecordEntity.enrollStatus
            )
        }
    }

    fun countStudentEnrolledEventRecord(eventUuid: String): TotalCountDto {
        val eventUuidObj: UUID = UUID.fromString(eventUuid)
        eventRepository.findById(eventUuidObj).orElseThrow {
            Exception("Event with id: $eventUuid is not found")
        }

        return TotalCountDto(enrolledEventRecordRepository.countById_EventUuid(eventUuidObj))
    }
}