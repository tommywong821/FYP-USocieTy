package ngok3.fyp.backend.enrolled_event_record

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

}