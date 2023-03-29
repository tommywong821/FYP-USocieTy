package ngok3.fyp.backend.operation.attendance.model

data class StudentAttendanceDto(
    val studentUuid: String? = "",
    val studentItsc: String? = "",
    val studentName: String? = "",
    val attendanceCreatedAt: String = "",
    val eventName: String? = ""
)
