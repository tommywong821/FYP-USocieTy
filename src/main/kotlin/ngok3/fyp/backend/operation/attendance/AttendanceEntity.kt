package ngok3.fyp.backend.operation.attendance

import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "attendance")
open class AttendanceEntity {
    @EmbeddedId
    open var attendanceKey: AttendanceKey? = null

    @Column(name = "is_show")
    open var isShow: Boolean? = null

    open var updatedAt: LocalDateTime? = null
    open var createdAt: LocalDateTime? = null

    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity? = null

    @ManyToOne
    @MapsId("eventUuid")
    @JoinColumn(name = "event_entity_uuid")
    open var eventEntity: EventEntity? = null

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now()
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = LocalDateTime.now()
    }
}