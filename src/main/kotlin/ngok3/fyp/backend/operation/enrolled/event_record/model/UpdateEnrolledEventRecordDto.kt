package ngok3.fyp.backend.operation.enrolled.event_record.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus

data class UpdateEnrolledEventRecordDto(
    val eventId: String,
    val studentId: String,
    val status: EnrolledStatus,
)
