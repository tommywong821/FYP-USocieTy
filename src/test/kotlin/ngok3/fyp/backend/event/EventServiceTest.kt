package ngok3.fyp.backend.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.event.EventService
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.s3.S3Service
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.student.MockStudentRepository
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EventServiceTest {

    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()

    private val eventRepository: EventRepository = mockk()
    private val studentRepository: StudentRepository = mockk()
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk()
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()
    private val jwtUtil: JWTUtil = JWTUtil(enrolledSocietyRecordRepository = enrolledSocietyRecordRepository)
    private val s3Service: S3Service = mockk()
    private val eventService: EventService =
        EventService(eventRepository, studentRepository, enrolledEventRecordRepository, jwtUtil, s3Service)

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
}