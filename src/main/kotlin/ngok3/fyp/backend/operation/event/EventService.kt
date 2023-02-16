package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.enrolled_event_record.*
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.s3.S3BulkResponseEntity
import ngok3.fyp.backend.operation.s3.S3Service
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*


@Service
class EventService(
    private val eventRepository: EventRepository,
    private val studentRepository: StudentRepository,
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val jwtUtil: JWTUtil,
    private val s3Service: S3Service,
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

    fun createEvent(jwtToken: String, uploadFile: MultipartFile, eventDto: EventDto, societyName: String): EventEntity {
        //check if user belongs that society
        jwtUtil.verifyUserEnrolledSociety(jwtToken, societyName)
        //upload poster to s3
        val s3BulkResponseEntity: List<S3BulkResponseEntity> =
            s3Service.uploadFiles("${societyName}/event/", arrayOf(uploadFile))
        if (s3BulkResponseEntity.isEmpty() || !s3BulkResponseEntity[0].successful) {
            throw Exception("Upload File to AWS S3 failed")
        }
        //map aws s3 file name to database
        eventDto.poster = s3BulkResponseEntity[0].fileKey
        return eventRepository.save(eventDto.toEntity())
    }

    fun countEnrolledEvent(itsc: String): Long {
        return enrolledEventRecordRepository.countByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqual(
            itsc,
            LocalDateTime.now()
        )
    }
}