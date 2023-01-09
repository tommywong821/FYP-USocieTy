package ngok3.fyp.backend.enrolled_event_record

import ngok3.fyp.backend.event.EventEntity
import ngok3.fyp.backend.student.StudentEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_event_record")
open class EnrolledEventRecordEntity(
    @EmbeddedId
    open var id: EnrolledEventRecordKey? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: EnrolledStatus? = null
) {

    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    open var studentEntity: StudentEntity? = null

    @ManyToOne
    @MapsId("eventUuid")
    @JoinColumn(name = "event_entity_uuid")
    open var eventEntity: EventEntity? = null

}