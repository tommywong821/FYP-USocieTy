package ngok3.fyp.backend.util.entity

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.time.ZoneId
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
    open var updatedAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
    open var createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
) {

    @PreUpdate
    fun updateUpdatedAt() {
        this.updatedAt = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    }

    @PrePersist
    fun updateCreatedAt() {
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    }
}