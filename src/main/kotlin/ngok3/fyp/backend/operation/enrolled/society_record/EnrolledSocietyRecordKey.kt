package ngok3.fyp.backend.operation.enrolled.society_record

import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class EnrolledSocietyRecordKey(
    @Column(name = "student_entity_uuid")
    var studentUuid: UUID = UUID.randomUUID(),

    @Column(name = "society_entity_uuid")
    var societyUuid: UUID = UUID.randomUUID()
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EnrolledSocietyRecordKey

        return studentUuid == other.studentUuid
                && societyUuid == other.societyUuid
    }

    override fun hashCode(): Int = Objects.hash(studentUuid, societyUuid);
}