package ngok3.fyp.backend.event

import ngok3.fyp.backend.util.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "event")
class EventEntity(
    var name: String? = null,
    var poster: String? = null,
    var maxParticipation: Int? = null,
    var applyDeadline: LocalDateTime? = null
) : BaseEntity() {}