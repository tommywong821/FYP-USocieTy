package ngok3.fyp.backend.authentication.role

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "role")
open class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int? = null,
    @Enumerated(EnumType.STRING)
    open var role: Role = Role.ROLE_STUDENT
)