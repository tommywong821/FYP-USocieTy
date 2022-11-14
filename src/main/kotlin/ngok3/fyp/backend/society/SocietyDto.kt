package ngok3.fyp.backend.society

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

/**
 * A DTO for the {@link ngok3.fyp.backend.society.SocietyEntity} entity
 */
data class SocietyDto(
    val uuid: UUID? = null,
    val updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    val name: String? = null
) : Serializable