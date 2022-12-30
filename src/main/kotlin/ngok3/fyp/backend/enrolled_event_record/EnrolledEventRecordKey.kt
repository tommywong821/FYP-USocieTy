package ngok3.fyp.backend.enrolled_event_record

import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class EnrolledEventRecordKey(
    @Column(name = "student_entity_uuid")
    var studentUuid: UUID? = null,

    @Column(name = "event_entity_uuid")
    var eventUuid: UUID? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EnrolledEventRecordKey

        return studentUuid == other.studentUuid
                && eventUuid == other.eventUuid
    }

    override fun hashCode(): Int = Objects.hash(studentUuid, eventUuid);
}