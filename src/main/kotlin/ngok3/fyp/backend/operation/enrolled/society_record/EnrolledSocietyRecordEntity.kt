package ngok3.fyp.backend.operation.enrolled.society_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_society_record")
open class EnrolledSocietyRecordEntity(
    @EmbeddedId
    open var id: EnrolledSocietyRecordKey = EnrolledSocietyRecordKey(),

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: EnrolledStatus = EnrolledStatus.PENDING,

    open var updatedAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
    open var createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
) {
    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity = StudentEntity()

    @ManyToOne
    @MapsId("societyUuid")
    @JoinColumn(name = "society_entity_uuid")
    open var societyEntity: SocietyEntity = SocietyEntity()

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    }
}