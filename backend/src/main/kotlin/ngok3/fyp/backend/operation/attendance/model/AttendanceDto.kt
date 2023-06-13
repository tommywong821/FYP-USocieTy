package ngok3.fyp.backend.operation.attendance.model

data class AttendanceDto(
    val eventId: String,
    val studentId: String,
    val userItsc: String,
    val currentTime: String
)