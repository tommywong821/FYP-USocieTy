package ngok3.fyp.backend.event

import ngok3.fyp.backend.society.SocietyEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "event")
open class EventEntity(
    open var name: String? = null,
    open var poster: String? = null,
    open var maxParticipation: Int? = null,
    open var applyDeadline: LocalDateTime? = null,
    open var location: String? = null,
    open var date: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "society_entity_uuid")
    open var societyEntity: SocietyEntity? = null
) : BaseEntity()