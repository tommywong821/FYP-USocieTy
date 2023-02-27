package ngok3.fyp.backend.operation.attendance

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AttendanceService(
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository,
    private val attendanceRepository: AttendanceRepository
) {
    fun createAttendance(studentId: String, eventId: String) {
        enrolledEventRecordRepository.findByIdAndStatus(
            EnrolledEventRecordKey(studentUuid = UUID.fromString(studentId), eventUuid = UUID.fromString(eventId)),
            EnrolledStatus.SUCCESS
        ).orElseThrow {
            Exception("Student: $studentId did not enrolled Event: $eventId")
        }

        val attendanceKey: AttendanceKey = AttendanceKey()
        attendanceKey.eventUuid = UUID.fromString(eventId)
        attendanceKey.studentUuid = UUID.fromString(studentId)

        val attendanceEntity: AttendanceEntity = AttendanceEntity()
        attendanceEntity.attendanceKey = attendanceKey

        attendanceRepository.save(attendanceEntity)
    }
}