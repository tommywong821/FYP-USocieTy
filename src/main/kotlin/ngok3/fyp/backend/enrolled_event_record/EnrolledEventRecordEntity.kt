package ngok3.fyp.backend.enrolled_event_record

import ngok3.fyp.backend.event.EventEntity
import ngok3.fyp.backend.student.StudentEntity
import javax.persistence.*

@Entity
@Table(name = "enrolled_society_record")
class EnrolledEventRecordEntity(
    @Id
    @Column(nullable = false)
    var id: EnrolledEventRecordKey? = null,

    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_entity_uuid")
    var studentEntity: StudentEntity? = null,

    @ManyToOne
    @MapsId("eventUuid")
    @JoinColumn(name = "event_entity_uuid")
    var eventEntity: EventEntity? = null
) {

}