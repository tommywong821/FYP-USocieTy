package ngok3.fyp.backend.event

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import ngok3.fyp.backend.enrolled_event_record.EnrolledEventRepository
import ngok3.fyp.backend.student.MockStudentRepository
import ngok3.fyp.backend.student.StudentRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EventServiceTest {

    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()

    private val eventRepository: EventRepository = mockk()
    private val studentRepository: StudentRepository = mockk()
    private val enrolledEventRepository: EnrolledEventRepository = mockk()
    private val eventService: EventService = EventService(eventRepository, studentRepository, enrolledEventRepository)

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
            "",
            mockEventRepository.testPageNumWithoutSid,
            mockEventRepository.testPageSizeWithoutSid
        )

        val expectedResult = mockEventRepository.allTestEventList.subList(
            mockEventRepository.testPageNumWithoutSid,
            mockEventRepository.testPageSizeWithoutSid
        ).map { event ->
            EventDto(
                event.name,
                event.poster,
                event.maxParticipation,
                event.applyDeadline?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                event.location,
                event.date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }

        assertIterableEquals(allEvent, expectedResult)
    }

    @Test
    fun `should get all event with invalid sid`() {
        val invalidItsc: String = "invalid"

        every { studentRepository.findByItsc(invalidItsc) } returns Optional.empty()

        val exception: Exception = assertThrows(Exception::class.java) {
            eventService.getAllSocietyEvent(
                invalidItsc,
                mockEventRepository.testPageNumWithoutSid,
                mockEventRepository.testPageSizeWithoutSid
            )
        }

        assertEquals("Student with $invalidItsc not found", exception.message)
    }

    @Test
    fun `should get all event with valid sid`() {
        every { studentRepository.findByItsc(mockStudentRepository.testItsc) } returns Optional.of(mockStudentRepository.testStudentEntity)
        every {
            eventRepository.findBySocietyEntity_EnrolledSocietyRecordEntities_StudentEntityOrderByApplyDeadlineAsc(
                mockStudentRepository.testStudentEntity,
                mockEventRepository.testPageableWithSid
            )
        } returns mockEventRepository.withSidTestEventPage

        val allEvent: List<EventDto> = eventService.getAllSocietyEvent(
            mockStudentRepository.testItsc,
            mockEventRepository.testPageNumWithSid,
            mockEventRepository.testPageSizeWithSid
        )

        val expectedResult = mockEventRepository.allTestEventList.subList(
            mockEventRepository.testFromIndex,
            mockEventRepository.testToIndex
        ).map { event ->
            EventDto(
                event.name,
                event.poster,
                event.maxParticipation,
                event.applyDeadline?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                event.location,
                event.date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
        }

        assertIterableEquals(allEvent, expectedResult)
    }
}