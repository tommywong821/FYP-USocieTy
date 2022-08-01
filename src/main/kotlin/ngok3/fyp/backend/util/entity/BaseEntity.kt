package ngok3.fyp.backend.util.entity

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    val uuid: UUID? = null
    var updatedAt: Date? = null
    var createdAt: Date? = null

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = Date()
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = Date()
    }
}