package ngok3.fyp.backend.operation.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.attendance.AttendanceEntity
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.s3.S3BulkResponseEntity
import ngok3.fyp.backend.operation.s3.S3Service
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.InputStream
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class EventServiceTest {

    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    private val eventRepository: EventRepository = mockk()
    private val studentRepository: StudentRepository = mockk()
    private val societyRecordRepository: SocietyRepository = mockk()
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk()
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk()
    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)
    private val dateUtil: DateUtil = DateUtil()
    private val s3Service: S3Service = mockk()
    private val eventService: EventService =
        EventService(
            eventRepository,
            studentRepository,
            societyRecordRepository,
            enrolledEventRecordRepository,
            jwtUtil,
            dateUtil,
            s3Service
        )

    @Test
    fun `should get all event without sid`() {
        every {
            eventRepository.findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
                any(),
                mockEventRepository.testPageableWithoutSid
            )
        } returns mockEventRepository.withoutSidTestEventPage

        val allEvent: List<EventDto> = eventService.getAllEvent(
            mockEventRepository.testPageNumWithoutSid,
            mockEventRepository.testPageSizeWithoutSid
        )

        val expectedResult = mockEventRepository.allTestEventList.subList(
            mockEventRepository.testPageNumWithoutSid,
            mockEventRepository.testPageSizeWithoutSid
        ).map { eventEntity ->
            EventDto().createFromEntity(eventEntity, "")
        }

        assertIterableEquals(expectedResult, allEvent)
    }

    @Test
    fun `should delete event with event id`() {
        val uuid: String = UUID.randomUUID().toString()
        val mockEventEntity: EventEntity = EventEntity()
        mockEventEntity.societyEntity = SocietyEntity(name = mockAuthRepository.testSocietyName)

        every {
            eventRepository.findById(UUID.fromString(uuid))
        } returns Optional.of(mockEventEntity)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            eventRepository.deleteById(UUID.fromString(uuid))
        } returns Unit

        eventService.deleteEvent(mockAuthRepository.validUserCookieToken, uuid)

        verify(exactly = 1) { eventRepository.deleteById(UUID.fromString(uuid)) }
        verify(exactly = 1) { eventRepository.findById(UUID.fromString(uuid)) }
        verify(exactly = 1) {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        }

    }

    @Test
    fun `should update event with event id`() {
        val uuid: String = UUID.randomUUID().toString()
        val mockEventEntity: EventEntity = EventEntity()
        mockEventEntity.societyEntity = SocietyEntity(name = mockAuthRepository.testSocietyName)

        val updateEventDto = EventDto(
            name = "update name",
            maxParticipation = 10,
            applyDeadline = "2022-01-12T12:10:10.222Z",
            location = "update location",
            startDate = "2022-01-10T12:10:10.222Z",
            endDate = "2022-01-15T12:10:10.222Z",
            category = EventCategory.OUTDOOR,
            description = "update description",
            fee = 12.3
        )

        every {
            eventRepository.findById(UUID.fromString(uuid))
        } returns Optional.of(mockEventEntity)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            s3Service.uploadFiles("${mockAuthRepository.testSocietyName}/event/", any(), 1)
        } returns listOf(S3BulkResponseEntity("", "", "", true, 200))

        every {
            s3Service.uploadFiles("${mockAuthRepository.testSocietyName}/event/", any())
        } returns listOf(S3BulkResponseEntity("", "", "", true, 200))

        every {
            eventRepository.save(mockEventEntity)
        } returns mockEventEntity

        eventService.updateEvent(
            mockAuthRepository.validUserCookieToken,
            uuid,
            updateEventDto,
            MockMultipartFile("test", InputStream.nullInputStream())
        )

        verify(exactly = 1) { eventRepository.findById(UUID.fromString(uuid)) }
        verify(exactly = 1) {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        }
        verify(exactly = 1) { eventRepository.save(mockEventEntity) }

    }

    @Test
    fun getEventWithUuid() {
        val uuid: String = UUID.randomUUID().toString()
        val societyEntity: SocietyEntity = SocietyEntity(mockAuthRepository.testSocietyName)
        val mockEventEntity: EventEntity = EventEntity(
            "test event 1",
            "test poster 1",
            10,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            "test location 1",
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        )
        mockEventEntity.societyEntity = societyEntity

        every {
            eventRepository.findById(UUID.fromString(uuid))
        } returns Optional.of(mockEventEntity)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        val eventDto: EventDto = eventService.getEventWithUuid(mockAuthRepository.validUserCookieToken, uuid)

        assertEquals(mockEventEntity.name, eventDto.name)
        assertEquals(mockEventEntity.societyEntity.name, eventDto.society)
        assertEquals(mockEventEntity.poster, eventDto.poster)
    }

    @Test
    fun getAttAttendanceOfEvent() {
        val uuid: String = UUID.randomUUID().toString()

        val attendanceDtoList: List<StudentAttendanceDto> = listOf(
            StudentAttendanceDto(
                studentName = "123",
                studentItsc = "itsc1",
                attendanceCreatedAt = dateUtil.convertLocalDateTimeToStringWithTime(LocalDateTime.now())
            ),
            StudentAttendanceDto(
                studentName = "234",
                studentItsc = "itsc2",
                attendanceCreatedAt = dateUtil.convertLocalDateTimeToStringWithTime(LocalDateTime.now().plusDays(1))
            ),

            )

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        val currentDate: LocalDateTime = LocalDateTime.now()

        val societyEntity: SocietyEntity = SocietyEntity()
        societyEntity.name = mockAuthRepository.testSocietyName

        val studentEntity: StudentEntity = StudentEntity()
        studentEntity.itsc = "itsc"
        studentEntity.nickname = "nickname"

        val attendanceEntity: AttendanceEntity = AttendanceEntity()
        attendanceEntity.studentEntity = studentEntity
        attendanceEntity.createdAt = currentDate

        val eventEntity: EventEntity = EventEntity()
        eventEntity.attendanceEntities.add(attendanceEntity)
        eventEntity.societyEntity = societyEntity

        every { eventRepository.findById(UUID.fromString(uuid)) } returns Optional.of(eventEntity)

        val result: List<StudentAttendanceDto> =
            eventService.getAttAttendanceOfEvent(mockAuthRepository.validUserCookieToken, uuid, 0, 10)

        assertEquals("itsc", result[0].studentItsc)
        assertEquals("nickname", result[0].studentName)
        assertEquals(dateUtil.convertLocalDateTimeToStringWithTime(currentDate), result[0].attendanceCreatedAt)

    }
}