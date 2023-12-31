package ngok3.fyp.backend.operation.attendance

import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntityRepository
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventEntityRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentEntityRepository
import ngok3.fyp.backend.util.DateUtil
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AttendanceService(
    private val enrolledEventRecordRepository: EnrolledEventRecordEntityRepository,
    private val studentRepository: StudentEntityRepository,
    private val eventRepository: EventEntityRepository,
    private val attendanceRepository: AttendanceEntityRepository,
    private val studentRoleEntityRepository: StudentRoleEntityRepository,
    private val dateUtil: DateUtil
) {
    fun createAttendance(studentId: String, eventId: String, userItsc: String, currentTime: String) {
//        studentRoleEntityRepository.findByUserItscAndUserRoleAndHisSocietyIsHoldingEvent(
//            userItsc,
//            Role.ROLE_SOCIETY_MEMBER,
//            UUID.fromString(eventId)
//        ).orElseThrow {
//            AccessDeniedException("User: $userItsc did not belong to this event's society member")
//        }

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

        val attendanceEntity: Optional<AttendanceEntity> = attendanceRepository.findById(attendanceKey)
        if (attendanceEntity.isPresent) {
            throw DuplicateKeyException("Attendance of event: $eventId and student: $studentId is created already")
        }

        val newAttendanceEntity: AttendanceEntity = AttendanceEntity()
        newAttendanceEntity.attendanceKey = attendanceKey
        newAttendanceEntity.eventEntity = eventEntity
        newAttendanceEntity.studentEntity = studentEntity
        newAttendanceEntity.createdAt = dateUtil.convertStringWithTimeStampToLocalDateTime(currentTime)
        attendanceRepository.save(newAttendanceEntity)
    }

    fun getAllAttendance(): List<StudentAttendanceDto> {
        return attendanceRepository.findAll().map { attendanceEntity: AttendanceEntity ->
            StudentAttendanceDto(
                studentUuid = attendanceEntity.studentEntity?.uuid.toString(),
                studentItsc = attendanceEntity.studentEntity?.itsc,
                attendanceCreatedAt = attendanceEntity.createdAt.toString(),
                eventName = attendanceEntity.eventEntity?.name
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