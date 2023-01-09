package ngok3.fyp.backend.event

import ngok3.fyp.backend.enrolled_event_record.*
import ngok3.fyp.backend.student.StudentEntity
import ngok3.fyp.backend.student.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class EventService(
    @Autowired val eventRepository: EventRepository,
    @Autowired val studentRepository: StudentRepository,
    @Autowired val enrolledEventRecordRepository: EnrolledEventRecordRepository
) {
    fun getAllEvent(pageNum: Int, pageSize: Int): List<EventDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)
        println("LocalDateTime.now(): ${LocalDateTime.now()}")

        //get all event
        val allEvent: List<EventEntity> = eventRepository.findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
            LocalDateTime.now(), firstPageNumWithPageSizeElement
        ).content

        return allEvent.map { event ->
            EventDto(event)
        }
    }

    fun joinEvent(itsc: String, eventId: String): Boolean {
        val studentEntityOptional: Optional<StudentEntity> = studentRepository.findByItsc(itsc)
        if (studentEntityOptional.isEmpty) {
            throw Exception("student with itsc:$itsc is not found")
        }

        val eventEntityOptional: Optional<EventEntity> = eventRepository.findById(UUID.fromString(eventId))
        if (eventEntityOptional.isEmpty) {
            throw Exception("Event with id:$eventId is not found")
        }

        val eventEntity: EventEntity = eventEntityOptional.get()
//        check maxParticipation and apply deadline
        val numberOfParticipation: Long = enrolledEventRecordRepository.countById_EventUuid(UUID.fromString(eventId));
        if (LocalDateTime.now()
                .isAfter(eventEntity.applyDeadline) || numberOfParticipation >= eventEntity.maxParticipation!!
        ) {
            throw Exception("Event is not able to register")
        }

        val studentEntity: StudentEntity = studentEntityOptional.get()
        val enrolledEventRecordEntity = EnrolledEventRecordEntity(
            EnrolledEventRecordKey(studentEntity.uuid, eventEntity.uuid), EnrolledStatus.PENDING
        )
        enrolledEventRecordEntity.studentEntity = studentEntity
        enrolledEventRecordEntity.eventEntity = eventEntity
        enrolledEventRecordRepository.save(enrolledEventRecordEntity)
        return true;
    }

    fun getAllEnrolledEvent(itsc: String, pageNum: Int, pageSize: Int): List<EnrolledEventDto> {
        return enrolledEventRecordRepository.findByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqualOrderByEventEntity_StartDateAsc(
            itsc,
            LocalDateTime.now()
        ).map { enrolledEventEntity -> EnrolledEventDto(enrolledEventEntity) }
    }
}