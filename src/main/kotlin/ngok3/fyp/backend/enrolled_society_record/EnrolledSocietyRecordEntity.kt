package ngok3.fyp.backend.enrolled_society_record

import ngok3.fyp.backend.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.society.SocietyEntity
import ngok3.fyp.backend.student.StudentEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_society_record")
open class EnrolledSocietyRecordEntity(
    @EmbeddedId
    open var id: EnrolledSocietyRecordKey? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: EnrolledStatus? = null
) {
    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity? = null

    @ManyToOne
    @MapsId("societyUuid")
    @JoinColumn(name = "society_entity_uuid")
    open var societyEntity: SocietyEntity? = null
}