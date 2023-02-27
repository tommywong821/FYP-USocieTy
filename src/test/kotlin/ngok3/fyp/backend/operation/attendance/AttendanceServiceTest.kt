package ngok3.fyp.backend.operation.attendance

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class AttendanceServiceTest {

    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk(relaxed = true)
    private val studentRepository: StudentRepository = mockk(relaxed = true)
    private val eventRepository: EventRepository = mockk(relaxed = true)
    private val attendanceRepository: AttendanceRepository = mockk(relaxed = true)

    private val attendanceService: AttendanceService = AttendanceService(
        enrolledEventRecordRepository = enrolledEventRecordRepository,
        studentRepository = studentRepository,
        eventRepository = eventRepository,
        attendanceRepository = attendanceRepository
    )


    @Test
    fun `should create attendance with event id and student id`() {
        val studentId: UUID = UUID.fromString("cead8c1e-7cbe-44c6-8fc1-dabe57c80168")
        val eventId: UUID = UUID.fromString("6c8180b4-0681-4d88-950f-c8f16859f9d6")

        val attendanceEntity: AttendanceEntity = AttendanceEntity()
        val studentEntity: StudentEntity = StudentEntity()
        val eventEntity: EventEntity = EventEntity()


        every {
            enrolledEventRecordRepository.findByIdAndStatus(
                EnrolledEventRecordKey(studentUuid = studentId, eventUuid = eventId),
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledEventRecordEntity())

        every {
            studentRepository.findById(studentId)
        } returns Optional.of(studentEntity)

        every {
            eventRepository.findById(eventId)
        } returns Optional.of(eventEntity)

        every {
            attendanceRepository.save(any())
        } returns attendanceEntity

        attendanceService.createAttendance(studentId = studentId.toString(), eventId = eventId.toString())

        verify(exactly = 1) {
            enrolledEventRecordRepository.findByIdAndStatus(
                EnrolledEventRecordKey(studentUuid = studentId, eventUuid = eventId),
                EnrolledStatus.SUCCESS
            )
        }
        verify(exactly = 1) { attendanceRepository.save(any()) }
        verify(exactly = 1) { studentRepository.findById(studentId) }
        verify(exactly = 1) { eventRepository.findById(eventId) }
    }
}