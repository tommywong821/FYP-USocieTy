package ngok3.fyp.backend.enrolled_society_record

import ngok3.fyp.backend.society.SocietyEntity
import ngok3.fyp.backend.student.StudentEntity
import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_society_record")
open class EnrolledSocietyRecordEntity(
    @EmbeddedId
    open var id: EnrolledSocietyRecordKey? = null,
) {
    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity? = null

    @ManyToOne
    @MapsId("societyUuid")
    @JoinColumn(name = "society_entity_uuid")
    open var societyEntity: SocietyEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EnrolledSocietyRecordEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id);
}