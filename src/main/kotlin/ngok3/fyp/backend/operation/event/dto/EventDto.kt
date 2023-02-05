package ngok3.fyp.backend.operation.event.dto

import ngok3.fyp.backend.operation.event.EventEntity
import java.io.Serializable
import java.time.format.DateTimeFormatter

/**
 * A DTO for the {@link ngok3.fyp.backend.event.EventEntity} entity
 */
data class EventDto(
    val id: String? = "",
    val name: String? = "",
    val poster: String? = "",
    val maxParticipation: Int? = -1,
    val applyDeadline: String? = "",
    val location: String? = "",
    val startDate: String? = "",
    val endDate: String? = "",
    val category: String? = "",
    val description: String? = "",
    val fee: Double? = 0.0,
) : Serializable {
    constructor(eventEntity: EventEntity) : this(
        eventEntity.uuid.toString(),
        eventEntity.name,
        eventEntity.poster,
        eventEntity.maxParticipation,
        eventEntity.applyDeadline?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        eventEntity.location,
        eventEntity.startDate?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        eventEntity.endDate?.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        eventEntity.category,
        eventEntity.description,
        eventEntity.fee
    )
}