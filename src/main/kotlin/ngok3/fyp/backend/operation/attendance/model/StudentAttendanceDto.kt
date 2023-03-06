package ngok3.fyp.backend.operation.attendance.model

data class StudentAttendanceDto(
    val studentUuid: String,
    val studentNickname: String?,
    val attendanceCreatedAt: String
)
