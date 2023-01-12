package ngok3.fyp.backend.operation.enrolled_event_record

import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "enrolled_event_record")
open class EnrolledEventRecordEntity(
    @EmbeddedId
    open var id: EnrolledEventRecordKey? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: EnrolledStatus = EnrolledStatus.PENDING
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