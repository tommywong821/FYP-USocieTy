package ngok3.fyp.backend.util.entity

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    @Column(name = "uuid", nullable = false)
    open val uuid: UUID = UUID.randomUUID(),
    open var updatedAt: LocalDateTime = LocalDateTime.now(),
    open var createdAt: LocalDateTime = LocalDateTime.now()
) {

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now()
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = LocalDateTime.now()
    }
}