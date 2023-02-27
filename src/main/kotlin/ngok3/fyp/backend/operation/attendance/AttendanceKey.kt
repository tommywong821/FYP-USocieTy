package ngok3.fyp.backend.operation.attendance

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
open class AttendanceKey : Serializable {
    @Column(name = "student_entity_uuid")
    open var studentUuid: UUID? = null

    @Column(name = "event_entity_uuid")
    open var eventUuid: UUID? = null

    companion object {
        private const val serialVersionUID = -4863302676672335388L
    }
}