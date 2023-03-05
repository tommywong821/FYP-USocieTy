package ngok3.fyp.backend.operation.enrolled.event_record

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class EnrolledEventRecordKey(
    @Column(name = "student_entity_uuid")
    var studentUuid: UUID = UUID.randomUUID(),

    @Column(name = "event_entity_uuid")
    var eventUuid: UUID = UUID.randomUUID()
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EnrolledEventRecordKey

        if (studentUuid != other.studentUuid) return false
        if (eventUuid != other.eventUuid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = studentUuid.hashCode()
        result = 31 * result + eventUuid.hashCode()
        return result
    }

    companion object {
        private const val serialVersionUID: Long = 3499546169436857551L
    }
}