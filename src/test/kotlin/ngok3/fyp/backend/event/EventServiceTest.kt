package ngok3.fyp.backend.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import ngok3.fyp.backend.enrolled_event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.student.MockStudentRepository
import ngok3.fyp.backend.student.StudentRepository
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EventServiceTest {

    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()

    private val eventRepository: EventRepository = mockk()
    private val studentRepository: StudentRepository = mockk()
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk()
    private val eventService: EventService =
        EventService(eventRepository, studentRepository, enrolledEventRecordRepository)

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

        val allEvent: List<EventDto> = eventService.getAllSocietyEvent(
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