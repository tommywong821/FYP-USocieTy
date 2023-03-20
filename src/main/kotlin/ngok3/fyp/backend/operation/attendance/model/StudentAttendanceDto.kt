package ngok3.fyp.backend.operation.attendance.model

data class StudentAttendanceDto(
    val studentUuid: String,
    val studentItsc: String?,
    val attendanceCreatedAt: String,
    val attendanceUpdatedAt: String,
    val eventName: String?
)
