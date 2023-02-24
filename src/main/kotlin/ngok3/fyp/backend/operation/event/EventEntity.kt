package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.attendance.AttendanceEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "event")
open class EventEntity(
    open var name: String = "",
    open var poster: String = "",
    open var maxParticipation: Int = -1,
    open var applyDeadline: LocalDateTime = LocalDateTime.now(),
    open var location: String = "",
    open var startDate: LocalDateTime = LocalDateTime.now(),
    open var endDate: LocalDateTime = LocalDateTime.now(),
    open var category: String = "",
    open var description: String = "",
    open var fee: Double = -1.0,

    ) : BaseEntity() {
    @OneToMany(mappedBy = "eventEntity")
    open var enrolledEventRecordEntity: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()

    @ManyToOne
    @JoinColumn(name = "society_uuid", nullable = false)
    open var societyEntity: SocietyEntity = SocietyEntity()

    @OneToMany(mappedBy = "eventEntity", orphanRemoval = true)
    open var attendanceEntities: MutableSet<AttendanceEntity> = mutableSetOf()
}