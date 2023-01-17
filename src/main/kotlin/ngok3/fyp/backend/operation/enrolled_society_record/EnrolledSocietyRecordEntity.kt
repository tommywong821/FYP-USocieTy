package ngok3.fyp.backend.operation.enrolled_society_record

import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_society_record")
open class EnrolledSocietyRecordEntity(
    @EmbeddedId
    open var id: EnrolledSocietyRecordKey? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: EnrolledStatus = EnrolledStatus.PENDING,

    open var updatedAt: LocalDateTime? = null,
    open var createdAt: LocalDateTime? = null
) {
    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity? = null

    @ManyToOne
    @MapsId("societyUuid")
    @JoinColumn(name = "society_entity_uuid")
    open var societyEntity: SocietyEntity? = null

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now()
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = LocalDateTime.now()
    }
}