package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.s3.S3BulkResponseEntity
import ngok3.fyp.backend.operation.s3.S3Service
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.OptimisticLockException


@Service
class EventService(
    private val eventRepository: EventRepository,
    private val studentRepository: StudentRepository,
    private val societyRepository: SocietyRepository,
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val jwtUtil: JWTUtil,
    private val dateUtil: DateUtil,
    private val s3Service: S3Service,
) {

    @Value("\${aws.bucket.domain}")
    val s3BucketDomain: String = ""

    fun getAllEvent(pageNum: Int, pageSize: Int): List<EventDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)
        println("LocalDateTime.now(ZoneId.of(\"Asia/Hong_Kong\")): ${LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))}")

        //get all event
        val allEvent: List<EventEntity> = eventRepository.findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")), firstPageNumWithPageSizeElement
        ).content

        return allEvent.map { event ->
            EventDto().createFromEntity(event, s3BucketDomain)
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
        if (LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
                .isAfter(eventEntity.applyDeadline) || numberOfParticipation >= eventEntity.maxParticipation
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
        return true
    }

    fun createEvent(jwtToken: String, uploadFile: MultipartFile, eventDto: EventDto, societyName: String): EventDto {
        //check if user belongs that society
        jwtUtil.verifyUserAdminRoleOfSociety(jwtToken, societyName)
//        check if society exist
        val societyEntityOpt: Optional<SocietyEntity> = societyRepository.findByName(societyName)
        if (societyEntityOpt.isEmpty) {
            throw Exception("Society $societyName is not exist")
        }

        //upload poster to s3
        val s3BulkResponseEntity: List<S3BulkResponseEntity> =
            s3Service.uploadFiles("${societyName}/event/", arrayOf(uploadFile))
        if (s3BulkResponseEntity.isEmpty() || !s3BulkResponseEntity[0].successful) {
            throw Exception("Upload File to AWS S3 failed")
        }
        //map aws s3 file name to database
        eventDto.poster = s3BulkResponseEntity[0].fileKey
        val saveEventEntity: EventEntity = eventDto.toEntity()
        saveEventEntity.societyEntity = societyEntityOpt.get()
        eventRepository.save(saveEventEntity)

        return eventDto
    }

    fun deleteEvent(jwtToken: String, eventId: String) {
        try {
            jwtUtil.verifyUserAdminRoleOfSociety(
                jwtToken,
                eventRepository.findById(UUID.fromString(eventId)).get().societyEntity.name
            )
        } catch (e: NoSuchElementException) {
            throw Exception("Event with id: $eventId is not exist")
        }

        eventRepository.deleteById(UUID.fromString(eventId))
    }

    fun updateEvent(jwtToken: String, eventId: String, updateEvent: EventDto, uploadFile: MultipartFile) {

        val eventEntityOpt: Optional<EventEntity> = eventRepository.findById(UUID.fromString(eventId))

        if (eventEntityOpt.isEmpty) {
            throw Exception("Event with id: $eventId is not exist")
        }
        val eventEntity: EventEntity = eventEntityOpt.get()
        //prevent concurrent update
        if (eventEntity.version != updateEvent.version) {
            throw OptimisticLockException("Expected version: ${eventEntity.version} but request version: ${updateEvent.version}")
        }

        //check user identify
        jwtUtil.verifyUserAdminRoleOfSociety(
            jwtToken,
            eventEntity.societyEntity.name
        )

        //update event entity
        val s3BulkResponseEntity: List<S3BulkResponseEntity> =
            s3Service.uploadFiles(
                "${eventEntity.societyEntity.name}/event/",
                arrayOf(uploadFile),
                eventEntity.version + 1
            )
        if (s3BulkResponseEntity.isEmpty() || !s3BulkResponseEntity[0].successful) {
            throw Exception("Upload File to AWS S3 failed")
        }
        //map aws s3 file name to database
        eventEntity.name = updateEvent.name
        eventEntity.maxParticipation = updateEvent.maxParticipation
        eventEntity.applyDeadline = dateUtil.convertStringWithTimeStampToLocalDateTime(updateEvent.applyDeadline)
        eventEntity.location = updateEvent.location
        eventEntity.startDate = dateUtil.convertStringWithTimeStampToLocalDateTime(updateEvent.startDate)
        eventEntity.endDate = dateUtil.convertStringWithTimeStampToLocalDateTime(updateEvent.endDate)
        eventEntity.category = updateEvent.category
        eventEntity.description = updateEvent.description
        eventEntity.fee = updateEvent.fee
        eventEntity.poster = s3BulkResponseEntity[0].fileKey

        eventRepository.save(eventEntity)
    }

    fun getEventWithUuid(jwtToken: String, eventUuid: String): EventDto {
        val eventEntity: EventEntity = eventRepository.findById(UUID.fromString(eventUuid)).orElseThrow {
            Exception("event: $eventUuid does not exist")
        }

        jwtUtil.verifyUserAdminRoleOfSociety(jwtToken, eventEntity.societyEntity.name)

        return EventDto().createFromEntity(eventEntity, s3BucketDomain)
    }
}