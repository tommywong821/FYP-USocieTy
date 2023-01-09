package ngok3.fyp.backend.enrolled_event_record

import java.io.Serializable
import java.time.format.DateTimeFormatter

data class EnrolledEventDto(
    val name: String? = "",
    val location: String? = "",
    val startDate: String? = "",
    val endDate: String? = "",
    val category: String? = "",
    val status: String? = "",
) : Serializable {
    constructor(enrolledEventRecordEntity: EnrolledEventRecordEntity) : this(
        enrolledEventRecordEntity.eventEntity?.name,
        enrolledEventRecordEntity.eventEntity?.location,
        enrolledEventRecordEntity.eventEntity?.startDate?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        enrolledEventRecordEntity.eventEntity?.endDate?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        enrolledEventRecordEntity.eventEntity?.category,
        enrolledEventRecordEntity.status?.status
    )
}
