package ngok3.fyp.backend.society

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.society.SocietyEntity} entity
 */
data class SocietyDto(
    val id: String? = null,
    val name: String? = null
) : Serializable {
    constructor(societyEntity: SocietyEntity) : this(
        societyEntity.uuid.toString(), societyEntity.name
    )
}