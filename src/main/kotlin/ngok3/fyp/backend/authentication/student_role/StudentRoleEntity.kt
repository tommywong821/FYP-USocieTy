package ngok3.fyp.backend.authentication.student_role

import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "student_role_entity")
open class StudentRoleEntity() : BaseEntity() {
    @ManyToOne
    @JoinColumn(name = "student_uuid")
    open var studentEntity: StudentEntity = StudentEntity()

    @ManyToOne
    @JoinColumn(name = "role_id")
    open var roleEntity: RoleEntity = RoleEntity()

    @ManyToOne
    @JoinColumn(name = "society_uuid")
    open var societyEntity: SocietyEntity? = null
}