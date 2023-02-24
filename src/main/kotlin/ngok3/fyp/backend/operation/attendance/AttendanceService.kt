package ngok3.fyp.backend.operation.attendance

import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AttendanceService(
    private val studentRepository: StudentRepository,
    private val eventRepository: EventRepository,
    private val attendanceRepository: AttendanceRepository
) {
    fun createAttendance(studentId: String, eventId: String) {
        val studentEntity: StudentEntity = studentRepository.findById(UUID.fromString(studentId)).orElseThrow {
            throw Exception("Student id: $studentId is not exist")
        }
        val eventEntity: EventEntity = eventRepository.findById(UUID.fromString(eventId)).orElseThrow {
            throw Exception("Event id: $eventId is not exist")
        }

        val attendanceKey: AttendanceKey = AttendanceKey()
        attendanceKey.eventUuid = eventEntity.uuid
        attendanceKey.studentUuid = studentEntity.uuid

        val attendanceEntity: AttendanceEntity = AttendanceEntity()
        attendanceEntity.attendanceKey = attendanceKey

        attendanceRepository.save(attendanceEntity)
    }
}