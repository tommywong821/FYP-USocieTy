package ngok3.fyp.backend.operation.society

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.society.SocietyEntity} entity
 */
data class SocietyDto(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val holdingEventNumber: Long? = -1
) : Serializable {
    constructor(societyEntity: SocietyEntity) : this(
        id = societyEntity.uuid.toString(), name = societyEntity.name, description = societyEntity.description
    )
}