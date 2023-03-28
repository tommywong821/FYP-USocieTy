package ngok3.fyp.backend.operation.enrolled.event_record.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.PaymentStatus
import ngok3.fyp.backend.operation.event.EventCategory
import java.io.Serializable

data class EnrolledEventDto(
    val name: String,
    val location: String,
    val startDate: String,
    val endDate: String,
    val category: EventCategory,
    val enrolledStatus: EnrolledStatus,
    val paymentStatus: PaymentStatus,
) : Serializable
