package ngok3.fyp.backend.authentication.role

import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "role")
open class RoleEntity(
//    TODO move student role table to new table key: student id, role id, society id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = -1,
    @Enumerated(EnumType.STRING)
    open var role: Role = Role.ROLE_STUDENT
) {
    @OneToMany(mappedBy = "roleEntity")
    open var studentRoleEntity: MutableSet<StudentRoleEntity> = mutableSetOf()
}