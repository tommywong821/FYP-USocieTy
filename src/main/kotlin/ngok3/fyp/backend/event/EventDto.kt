package ngok3.fyp.backend.event

import java.io.Serializable
import java.time.LocalDateTime

/**
 * A DTO for the {@link ngok3.fyp.backend.event.EventEntity} entity
 */
data class EventDto(
    val name: String? = null,
    val poster: String? = null,
    val maxParticipation: Int? = null,
    val applyDeadline: LocalDateTime? = null
) : Serializable