package ngok3.fyp.backend.event

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.event.EventEntity} entity
 */
data class EventDto(
    val name: String? = null,
    val poster: String? = null,
    val maxParticipation: Int? = null,
    val applyDeadline: String? = null,
    val location: String? = null,
    var date: String? = null
) : Serializable