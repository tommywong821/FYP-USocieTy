package ngok3.fyp.backend.student

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Student(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    val uuid: UUID? = null,
    var itsc: String? = null,
    var name: String? = null,
    var mail: String? = null,
    var role: String? = null
)
