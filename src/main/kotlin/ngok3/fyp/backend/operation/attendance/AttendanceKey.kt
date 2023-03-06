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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttendanceKey

        if (studentUuid != other.studentUuid) return false
        if (eventUuid != other.eventUuid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = studentUuid?.hashCode() ?: 0
        result = 31 * result + (eventUuid?.hashCode() ?: 0)
        return result
    }


}