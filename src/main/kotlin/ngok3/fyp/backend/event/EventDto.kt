package ngok3.fyp.backend.event

import java.io.Serializable
import java.time.format.DateTimeFormatter

/**
 * A DTO for the {@link ngok3.fyp.backend.event.EventEntity} entity
 */
data class EventDto(
    val name: String? = null,
    val poster: String? = null,
    val maxParticipation: Int? = null,
    val applyDeadline: String? = null,
    val location: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val category: String? = null,
    val description: String? = null,
    val fee: Long? = null,
) : Serializable {
    constructor(eventEntity: EventEntity) : this(
        eventEntity.name, eventEntity.poster, eventEntity.maxParticipation, eventEntity.applyDeadline?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ), eventEntity.location,
        eventEntity.startDate?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ), eventEntity.endDate?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ), eventEntity.category, eventEntity.description, eventEntity.fee
    )
}