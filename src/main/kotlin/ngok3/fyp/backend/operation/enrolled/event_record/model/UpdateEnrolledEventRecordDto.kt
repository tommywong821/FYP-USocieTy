package ngok3.fyp.backend.operation.enrolled.event_record.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.PaymentStatus

data class UpdateEnrolledEventRecordDto(
    val eventId: String,
    val studentId: String,
    val enrolledStatus: EnrolledStatus,
    val paymentStatus: PaymentStatus,
)
