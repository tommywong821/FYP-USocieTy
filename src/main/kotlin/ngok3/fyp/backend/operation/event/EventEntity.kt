package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.attendance.AttendanceEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import java.time.LocalDateTime
import java.time.ZoneId
import javax.persistence.*

@Entity
@Table(name = "event")
open class EventEntity(
    open var name: String = "",
    open var poster: String = "",
    open var maxParticipation: Int = -1,
    open var applyDeadline: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
    open var location: String = "",
    open var startDate: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
    open var endDate: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
    @Enumerated(EnumType.STRING) open var category: EventCategory = EventCategory.OUTDOOR,
    @Column(columnDefinition = "TEXT") open var description: String = "",
    open var fee: Double = -1.0,
    @Version open var version: Long = 0
) : BaseEntity() {


    @OneToMany(mappedBy = "eventEntity", cascade = [CascadeType.ALL])
    open var enrolledEventRecordEntity: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()

    @ManyToOne
    @JoinColumn(name = "society_uuid", nullable = false)
    open var societyEntity: SocietyEntity = SocietyEntity()

    @OneToMany(mappedBy = "eventEntity", cascade = [CascadeType.ALL])
    open var attendanceEntities: MutableSet<AttendanceEntity> = mutableSetOf()
}