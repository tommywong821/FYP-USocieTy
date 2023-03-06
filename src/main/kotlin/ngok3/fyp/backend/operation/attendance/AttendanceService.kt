package ngok3.fyp.backend.operation.attendance

import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AttendanceService(
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val studentRepository: StudentRepository,
    private val eventRepository: EventRepository,
    private val attendanceRepository: AttendanceRepository
) {
    fun createAttendance(studentId: String, eventId: String) {
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
                attendanceEntity.createdAt.toString()
            )
        }
    }
}