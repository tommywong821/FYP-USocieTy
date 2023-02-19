package ngok3.fyp.backend.operation.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.s3.S3Service
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.society.SocietyRepository
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class EventServiceTest {

    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    private val eventRepository: EventRepository = mockk()
    private val studentRepository: StudentRepository = mockk()
    private val societyRecordRepository: SocietyRepository = mockk()
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk()
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()
    private val jwtUtil: JWTUtil = JWTUtil(enrolledSocietyRecordRepository = enrolledSocietyRecordRepository)
    private val s3Service: S3Service = mockk()
    private val eventService: EventService =
        EventService(
            eventRepository,
            studentRepository,
            societyRecordRepository,
            enrolledEventRecordRepository,
            jwtUtil,
            s3Service
        )

    @Test
    fun `should get all event without sid`() {
        mockkStatic(LocalDateTime::class)
        every { LocalDateTime.now() } returns LocalDateTime.MAX

        every {
            eventRepository.findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
                LocalDateTime.now(),
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
            EventDto(eventEntity)
        }

        assertIterableEquals(allEvent, expectedResult)
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
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            eventRepository.deleteById(UUID.fromString(uuid))
        } returns Unit

        eventService.deleteEvent(mockAuthRepository.validUserCookieToken, uuid)

        verify(exactly = 1) { eventRepository.deleteById(UUID.fromString(uuid)) }
        verify(exactly = 1) { eventRepository.findById(UUID.fromString(uuid)) }
        verify(exactly = 1) {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        }

    }
}