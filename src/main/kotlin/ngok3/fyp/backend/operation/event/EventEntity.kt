package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "event")
open class EventEntity(
    open var name: String? = null,
    open var poster: String? = null,
    open var maxParticipation: Int? = null,
    open var applyDeadline: LocalDateTime? = null,
    open var location: String? = null,
    open var startDate: LocalDateTime? = null,
    open var endDate: LocalDateTime? = null,
    open var category: String? = null,
    open var description: String? = null,
    open var fee: Double? = null,

    ) : BaseEntity() {
    @OneToMany(mappedBy = "eventEntity")
    open var enrolledEventRecordEntity: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()
}