package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.operation.enrolled_event_record.*
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.event.dto.CreateEventDto
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class EventService(
    @Autowired val eventRepository: EventRepository,
    @Autowired val studentRepository: StudentRepository,
    @Autowired val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    @Autowired val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository,
    private val model: ModelMapper = ModelMapper()
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

    fun createEvent(createEventDto: CreateEventDto): EventEntity {
//        check if user itsc has society member role
        if (studentRepository.findByItscAndRoles_Role(createEventDto.itsc, Role.ROLE_SOCIETY_MEMBER).isEmpty) {
            throw AccessDeniedException("User ${createEventDto.itsc} do not have right to create event")
        }
//        check if user is in that society
        if (enrolledSocietyRecordRepository.findByStudentEntity_ItscAndSocietyEntity_NameAndStatus(
                createEventDto.itsc,
                createEventDto.society,
                EnrolledStatus.SUCCESS
            ).isEmpty
        ) {
            throw AccessDeniedException("User ${createEventDto.itsc} do not belong to ${createEventDto.society}")
        }
        val savedEventEntity: EventEntity = model.map(createEventDto.eventDto, EventEntity::class.java)
        return eventRepository.save(savedEventEntity)
    }

    fun countEnrolledEvent(itsc: String): Long {
        return enrolledEventRecordRepository.countByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqual(
            itsc,
            LocalDateTime.now()
        )
    }
}