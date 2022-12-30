package ngok3.fyp.backend.enrolled_event_record

import ngok3.fyp.backend.event.EventEntity
import ngok3.fyp.backend.student.StudentEntity
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_event_record")
open class EnrolledEventRecordEntity(
    @EmbeddedId
    open var id: EnrolledEventRecordKey? = null,

    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity? = null,

    @ManyToOne
    @MapsId("eventUuid")
    @JoinColumn(name = "event_entity_uuid")
    open var societyEntity: EventEntity? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EnrolledEventRecordEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id);
}