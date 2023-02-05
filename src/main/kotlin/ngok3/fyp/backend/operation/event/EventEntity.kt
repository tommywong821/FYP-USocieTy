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
    open var name: String? = "",
    open var poster: String? = "",
    open var maxParticipation: Int? = -1,
    open var applyDeadline: LocalDateTime? = LocalDateTime.MIN,
    open var location: String? = "",
    open var startDate: LocalDateTime? = LocalDateTime.MIN,
    open var endDate: LocalDateTime? = LocalDateTime.MIN,
    open var category: String? = "",
    open var description: String? = "",
    open var fee: Double? = -1.0,

    ) : BaseEntity() {
    @OneToMany(mappedBy = "eventEntity")
    open var enrolledEventRecordEntity: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()
}