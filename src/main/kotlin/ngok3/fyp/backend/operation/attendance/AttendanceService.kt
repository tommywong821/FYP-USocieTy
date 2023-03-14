package ngok3.fyp.backend.operation.attendance

import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AttendanceService(
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val studentRepository: StudentRepository,
    private val eventRepository: EventRepository,
    private val attendanceRepository: AttendanceRepository,
    private val studentRoleEntityRepository: StudentRoleEntityRepository
) {
    fun createAttendance(studentId: String, eventId: String, userItsc: String) {
        studentRoleEntityRepository.findByUserItscAndUserRoleAndHisSocietyIsHoldingEvent(
            userItsc,
            Role.ROLE_SOCIETY_MEMBER,
            UUID.fromString(eventId)
        ).orElseThrow {
            AccessDeniedException("User: $userItsc did not belong to this event's society member")
        }

        enrolledEventRecordRepository.findByIdAndStatus(
            EnrolledEventRecordKey(studentUuid = UUID.fromString(studentId), eventUuid = UUID.fromString(eventId)),
            EnrolledStatus.SUCCESS
        ).orElseThrow {
            Exception("Student: $studentId did not enrolled Event: $eventId")
        }

        val studentEntity: StudentEntity = studentRepository.findById(UUID.fromString(studentId)).orElseThrow {
            throw Exception("Student id: $studentId is not exist")
        }
        val eventEntity: EventEntity = eventRepository.findById(UUID.fromString(eventId)).orElseThrow {
            throw Exception("Event id: $eventId is not exist")
        }

        val attendanceKey: AttendanceKey = AttendanceKey()
        attendanceKey.eventUuid = UUID.fromString(eventId)
        attendanceKey.studentUuid = UUID.fromString(studentId)

        val attendanceEntity: AttendanceEntity = AttendanceEntity()
        attendanceEntity.attendanceKey = attendanceKey
        attendanceEntity.eventEntity = eventEntity
        attendanceEntity.studentEntity = studentEntity

        attendanceRepository.save(attendanceEntity)
    }

    fun getAllAttendance(): List<StudentAttendanceDto> {
        return attendanceRepository.findAll().map { attendanceEntity: AttendanceEntity ->
            StudentAttendanceDto(
                attendanceEntity.studentEntity?.uuid.toString(),
                attendanceEntity.studentEntity?.nickname,
                attendanceEntity.createdAt.toString(),
                attendanceEntity.eventEntity?.name
            )
        }
    }

    fun deleteAttendance(studentUuid: String, eventUuid: String): Boolean {
        val attendanceKey: AttendanceKey = AttendanceKey()
        attendanceKey.studentUuid = UUID.fromString(studentUuid)
        attendanceKey.eventUuid = UUID.fromString(eventUuid)
        return (attendanceRepository.deleteByAttendanceKey(attendanceKey) == 1)

    }
}