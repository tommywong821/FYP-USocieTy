package ngok3.fyp.backend.operation.enrolled.event_record.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.PaymentStatus

data class StudentEnrolledEventRecordDto(
    val studentId: String,
    val itsc: String,
    val paymentStatus: PaymentStatus,
    val enrolledStatus: EnrolledStatus,
)
