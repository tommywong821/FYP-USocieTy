package ngok3.fyp.backend.operation.enrolled_event_record

data class UpdateEnrolledEventRecordDto(
    val eventId: String,
    val studentId: String,
    val status: EnrolledStatus,
)
