package ngok3.fyp.backend.operation.enrolled.event_record.model

import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import java.io.Serializable
import java.time.format.DateTimeFormatter

data class EnrolledEventDto(
    val name: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val category: String? = "",
    val status: String = "",
    val paymentStatus: String = "",
) : Serializable {
    constructor(enrolledEventRecordEntity: EnrolledEventRecordEntity) : this(
        name = enrolledEventRecordEntity.eventEntity.name,
        location = enrolledEventRecordEntity.eventEntity.location,
        startDate = enrolledEventRecordEntity.eventEntity.startDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        endDate = enrolledEventRecordEntity.eventEntity.endDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        category = enrolledEventRecordEntity.eventEntity.category.category,
        status = enrolledEventRecordEntity.enrollStatus.status,
        paymentStatus = enrolledEventRecordEntity.paymentStatus.status
    )
}
