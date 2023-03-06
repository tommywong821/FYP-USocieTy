package ngok3.fyp.backend.authentication.student_role

import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import javax.persistence.*

@Entity
@Table(name = "student_role_entity")
open class StudentRoleEntity {
    @EmbeddedId
    open var studentRoleEntityKey: StudentRoleEntityKey? = null

    @ManyToOne
    @MapsId("studentUuid")
    @JoinColumn(name = "student_uuid")
    open var studentEntity: StudentEntity = StudentEntity()

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    open var roleEntity: RoleEntity = RoleEntity()

    @ManyToOne
    @JoinColumn(name = "society_uuid")
    open var societyEntity: SocietyEntity? = null
}