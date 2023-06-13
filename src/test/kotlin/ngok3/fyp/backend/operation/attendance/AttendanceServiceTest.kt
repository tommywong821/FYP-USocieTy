package ngok3.fyp.backend.operation.attendance

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntityRepository
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventEntityRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentEntityRepository
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class AttendanceServiceTest {

    private val enrolledEventRecordRepository: EnrolledEventRecordEntityRepository = mockk(relaxed = true)
    private val studentRepository: StudentEntityRepository = mockk(relaxed = true)
    private val eventRepository: EventEntityRepository = mockk(relaxed = true)
    private val attendanceRepository: AttendanceEntityRepository = mockk(relaxed = true)
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk(relaxed = true)

    private val dateUtil: DateUtil = DateUtil()

    private val attendanceService: AttendanceService = AttendanceService(
        enrolledEventRecordRepository = enrolledEventRecordRepository,
        studentRepository = studentRepository,
        eventRepository = eventRepository,
        attendanceRepository = attendanceRepository,
        studentRoleEntityRepository = studentRoleEntityRepository,
        dateUtil = dateUtil
    )


    @Test
    fun `should create attendance with event id and student id`() {
        val studentId: UUID = UUID.fromString("cead8c1e-7cbe-44c6-8fc1-dabe57c80168")
        val eventId: UUID = UUID.fromString("6c8180b4-0681-4d88-950f-c8f16859f9d6")
        val userItsc: String = "itsc"

        val attendanceEntity: AttendanceEntity = AttendanceEntity()
        val studentEntity: StudentEntity = StudentEntity()
        val eventEntity: EventEntity = EventEntity()

        every {
            studentRoleEntityRepository.findByUserItscAndUserRoleAndHisSocietyIsHoldingEvent(
                userItsc,
                Role.ROLE_SOCIETY_MEMBER,
                eventId
            )
        } returns Optional.of(StudentRoleEntity())

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
            attendanceRepository.findById(any())
        } returns Optional.empty()

        every {
            attendanceRepository.save(any())
        } returns attendanceEntity

        attendanceService.createAttendance(
            studentId = studentId.toString(),
            eventId = eventId.toString(),
            userItsc,
            currentTime = "2023-03-24T15:33:23.123Z"
        )

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

    @Test
    fun getAllAttendance() {
        val studentEntity1: StudentEntity = StudentEntity(nickname = "nickname 1")
        val eventEntity1: EventEntity = EventEntity(name = "test event 1")
        val attendanceEntity1: AttendanceEntity = AttendanceEntity()
        attendanceEntity1.studentEntity = studentEntity1
        attendanceEntity1.eventEntity = eventEntity1
        attendanceEntity1.createdAt = LocalDateTime.now()

        val studentEntity2: StudentEntity = StudentEntity(nickname = "nickname 2")
        val eventEntity2: EventEntity = EventEntity(name = "test event 2")
        val attendanceEntity2: AttendanceEntity = AttendanceEntity()
        attendanceEntity2.studentEntity = studentEntity2
        attendanceEntity2.eventEntity = eventEntity2
        attendanceEntity2.createdAt = LocalDateTime.now()

        val mockAttendanceEntityList: List<AttendanceEntity> = listOf(
            attendanceEntity1,
            attendanceEntity2
        )

        every {
            attendanceRepository.findAll()
        } returns mockAttendanceEntityList

        val studentAttendanceDtoList: List<StudentAttendanceDto> = attendanceService.getAllAttendance()

        Assertions.assertEquals(
            mockAttendanceEntityList[0].studentEntity?.uuid.toString(),
            studentAttendanceDtoList[0].studentUuid
        )
        Assertions.assertEquals(
            mockAttendanceEntityList[0].studentEntity?.itsc,
            studentAttendanceDtoList[0].studentItsc
        )
        Assertions.assertEquals(
            mockAttendanceEntityList[0].createdAt.toString(),
            studentAttendanceDtoList[0].attendanceCreatedAt
        )
        Assertions.assertEquals(
            mockAttendanceEntityList[0].eventEntity?.name,
            studentAttendanceDtoList[0].eventName
        )

        Assertions.assertEquals(
            mockAttendanceEntityList[1].studentEntity?.uuid.toString(),
            studentAttendanceDtoList[1].studentUuid
        )
        Assertions.assertEquals(
            mockAttendanceEntityList[1].studentEntity?.itsc,
            studentAttendanceDtoList[1].studentItsc
        )
        Assertions.assertEquals(
            mockAttendanceEntityList[1].createdAt.toString(),
            studentAttendanceDtoList[1].attendanceCreatedAt
        )
        Assertions.assertEquals(
            mockAttendanceEntityList[1].eventEntity?.name,
            studentAttendanceDtoList[1].eventName
        )
    }
}