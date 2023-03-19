package ngok3.fyp.backend.operation.enrolled.event_record.model

data class StudentEnrolledEventRecordDto(
    val studentId: String,
    val itsc: String,
    val paymentStatus: String,
    val enrolledStatus: String,
)
