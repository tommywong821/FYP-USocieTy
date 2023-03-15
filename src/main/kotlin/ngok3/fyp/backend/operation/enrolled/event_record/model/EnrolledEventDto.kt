package ngok3.fyp.backend.operation.enrolled.event_record.model

import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.event.EventCategory
import java.io.Serializable
import java.time.format.DateTimeFormatter

data class EnrolledEventDto(
    val name: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val category: EventCategory? = null,
    val status: String = "",
) : Serializable {
    constructor(enrolledEventRecordEntity: EnrolledEventRecordEntity) : this(
        enrolledEventRecordEntity.eventEntity.name,
        enrolledEventRecordEntity.eventEntity.location,
        enrolledEventRecordEntity.eventEntity.startDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        enrolledEventRecordEntity.eventEntity.endDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        enrolledEventRecordEntity.eventEntity.category,
        enrolledEventRecordEntity.status.status
    )
}
