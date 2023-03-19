package ngok3.fyp.backend.operation.enrolled.event_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_event_record")
open class EnrolledEventRecordEntity(
    @EmbeddedId
    open var id: EnrolledEventRecordKey = EnrolledEventRecordKey(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: EnrolledStatus = EnrolledStatus.PENDING,

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus")
    open var paymentStatus: PaymentStatus = PaymentStatus.UNPAID,

    open var updatedAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
    open var createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
) {

    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity = StudentEntity()

    @ManyToOne
    @MapsId("eventUuid")
    @JoinColumn(name = "event_entity_uuid")
    open var eventEntity: EventEntity = EventEntity()

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    }
}