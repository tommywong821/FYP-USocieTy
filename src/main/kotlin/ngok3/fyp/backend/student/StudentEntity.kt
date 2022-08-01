package ngok3.fyp.backend.student

import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity

@Entity(name = "student")
class StudentEntity(
    var itsc: String? = null,
    var name: String? = null,
    var mail: String? = null,
    var role: String? = null
) : BaseEntity(
)
