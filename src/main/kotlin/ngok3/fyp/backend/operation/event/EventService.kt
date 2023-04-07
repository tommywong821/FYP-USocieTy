package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.TotalCountDto
import ngok3.fyp.backend.operation.attendance.AttendanceEntity
import ngok3.fyp.backend.operation.attendance.AttendanceEntityRepository
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntityRepository
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.PaymentStatus
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.s3.S3BulkResponseEntity
import ngok3.fyp.backend.operation.s3.S3Service
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyEntityRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentEntityRepository
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import ngok3.fyp.backend.util.exception.model.FlutterException
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
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
    private val eventEntityRepository: EventEntityRepository,
    private val studentRepository: StudentEntityRepository,
    private val societyRepository: SocietyEntityRepository,
    private val enrolledEventRecordRepository: EnrolledEventRecordEntityRepository,
    private val jwtUtil: JWTUtil,
    private val dateUtil: DateUtil,
    private val s3Service: S3Service, private val attendanceEntityRepository: AttendanceEntityRepository,
) {

    @Value("\${aws.bucket.domain}")
    val s3BucketDomain: String = ""

    fun getAllEvent(pageNum: Int, pageSize: Int): List<EventDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)

        //get all event
        val allEvent: List<EventEntity> =
            eventEntityRepository.findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
                LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")), firstPageNumWithPageSizeElement
            ).content

//        find event holding event
        val holdingEventNumberMap: Map<String, Long?> =
            societyRepository.findHoldingEventNumberOfSociety(LocalDateTime.now())
                .associateBy({ it.name }, { it.holdingEventNumber })

        val eventDtoList: List<EventDto> = allEvent.map { event ->
            EventDto().createFromEntity(event, s3BucketDomain)
        }

//        combine event dto and its holding event number
        eventDtoList.forEach { eventDto: EventDto ->
            run {
                if (holdingEventNumberMap.containsKey(eventDto.society)) {
                    eventDto.societyHoldingEventNumber = holdingEventNumberMap[eventDto.society]
                }
            }
        }

        return eventDtoList
    }

    fun joinEvent(itsc: String, eventId: String): Boolean {
        val studentEntity: StudentEntity = studentRepository.findByItsc(itsc).orElseThrow {
            DuplicateKeyException("student with itsc:$itsc is not found")
        }

        val eventEntity: EventEntity = eventEntityRepository.findById(UUID.fromString(eventId)).orElseThrow {
            DuplicateKeyException("Event with id:$eventId is not found")
        }


//        check maxParticipation and apply deadline
        val numberOfParticipation: Long = enrolledEventRecordRepository.countById_EventUuid(UUID.fromString(eventId))
        if (LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
                .isAfter(eventEntity.applyDeadline) || numberOfParticipation >= eventEntity.maxParticipation
        ) {
            throw FlutterException("Event is not able to register")
        }

//        check if already join if joined 400 bad request
        val enrolledEventEntityKey: EnrolledEventRecordKey =
            EnrolledEventRecordKey(studentEntity.uuid, eventEntity.uuid)
        if (enrolledEventRecordRepository.findById(enrolledEventEntityKey).isPresent) {
            throw DuplicateKeyException("Event is already register")
        }

        val enrolledEventRecordEntity = EnrolledEventRecordEntity(
            id = enrolledEventEntityKey, enrollStatus = EnrolledStatus.PENDING
        )
        enrolledEventRecordEntity.studentEntity = studentEntity
        enrolledEventRecordEntity.eventEntity = eventEntity
        enrolledEventRecordEntity.paymentStatus = PaymentStatus.UNPAID
        enrolledEventRecordRepository.save(enrolledEventRecordEntity)
        return true
    }

    fun createEvent(jwtToken: String, uploadFile: MultipartFile, eventDto: EventDto, societyName: String): EventDto {
        //check if user belongs that society
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)
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
        eventEntityRepository.save(saveEventEntity)

        return eventDto
    }

    fun deleteEvent(jwtToken: String, eventId: String) {
        try {
            jwtUtil.verifyUserMemberRoleOfSociety(
                jwtToken,
                eventEntityRepository.findById(UUID.fromString(eventId)).get().societyEntity.name
            )
        } catch (e: NoSuchElementException) {
            throw Exception("Event with id: $eventId is not exist")
        }

        eventEntityRepository.deleteById(UUID.fromString(eventId))
    }

    fun updateEvent(jwtToken: String, eventId: String, updateEvent: EventDto, uploadFile: MultipartFile?) {

        val eventEntityOpt: Optional<EventEntity> = eventEntityRepository.findById(UUID.fromString(eventId))

        if (eventEntityOpt.isEmpty) {
            throw Exception("Event with id: $eventId is not exist")
        }
        val eventEntity: EventEntity = eventEntityOpt.get()
        //prevent concurrent update
        if (eventEntity.version != updateEvent.version) {
            throw OptimisticLockException("Expected version: ${eventEntity.version} but request version: ${updateEvent.version}")
        }

        //check user identify
        jwtUtil.verifyUserMemberRoleOfSociety(
            jwtToken,
            eventEntity.societyEntity.name
        )

        //update event entity
        if (uploadFile != null) {
            val s3BulkResponseEntity: List<S3BulkResponseEntity> =
                s3Service.uploadFiles(
                    "${eventEntity.societyEntity.name}/event/",
                    arrayOf(uploadFile),
                    eventEntity.version + 1
                )
            if (s3BulkResponseEntity.isEmpty() || !s3BulkResponseEntity[0].successful) {
                throw Exception("Upload File to AWS S3 failed")
            }
            eventEntity.poster = s3BulkResponseEntity[0].fileKey
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

        eventEntityRepository.save(eventEntity)
    }

    fun getEventWithUuid(jwtToken: String, eventUuid: String): EventDto {
        val eventEntity: EventEntity = eventEntityRepository.findById(UUID.fromString(eventUuid)).orElseThrow {
            Exception("event: $eventUuid does not exist")
        }

        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, eventEntity.societyEntity.name)

        return EventDto().createFromEntity(eventEntity, s3BucketDomain)
    }

    fun getEventWithSocietyName(jwtToken: String, societyName: String, pageNum: Int, pageSize: Int): List<EventDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

        return eventEntityRepository.findAllBySocietyName(societyName, PageRequest.of(pageNum, pageSize))
            .map { eventEntity: EventEntity ->
                EventDto().createFromEntity(eventEntity, s3BucketDomain)
            }
    }

    fun getAllAttendanceOfEvent(
        jwtToken: String,
        eventId: String,
        pageNum: Int,
        pageSize: Int
    ): List<StudentAttendanceDto> {
        val eventEntity: EventEntity = eventEntityRepository.findById(UUID.fromString(eventId)).orElseThrow {
            Exception("Event: $eventId is not exist")
        }

        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, eventEntity.societyEntity.name)

        return attendanceEntityRepository.findByAttendanceKey_EventUuid(
            eventEntity.uuid,
            PageRequest.of(pageNum, pageSize)
        ).map { attendanceEntity: AttendanceEntity ->
            StudentAttendanceDto(
                studentItsc = attendanceEntity.studentEntity?.itsc,
                studentName = attendanceEntity.studentEntity?.nickname,
                attendanceCreatedAt = dateUtil.convertLocalDateTimeToStringWithTime(attendanceEntity.createdAt)
            )
        }
    }

    fun getTotalNumberOfAllAttendanceOfEvent(jwtToken: String, eventUuid: String): TotalCountDto {
        val eventEntity: EventEntity = eventEntityRepository.findById(UUID.fromString(eventUuid)).orElseThrow {
            Exception("Event: $eventUuid is not exist")
        }

        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, eventEntity.societyEntity.name)

        return TotalCountDto(attendanceEntityRepository.countByAttendanceKey_EventUuid(eventUuid = eventEntity.uuid))
    }
}