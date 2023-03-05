package ngok3.fyp.backend.authentication.student_role

import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
open class StudentRoleEntityKey : Serializable {
    @Column(name = "student_entity_uuid")
    open var studentUuid: UUID? = null

    @Column(name = "role_id")
    open var roleId: Int? = null
    override fun hashCode(): Int = Objects.hash(studentUuid, roleId)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as StudentRoleEntityKey

        return studentUuid == other.studentUuid &&
                roleId == other.roleId
    }

    companion object {
        private const val serialVersionUID = 2594592787112959031L
    }
}