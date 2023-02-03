package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "finance")
open class FinanceEntity(
    open var amount: Number? = 0.0,
    open var description: String? = null,
    open var date: LocalDateTime? = null,
    open var category: String? = null,
) : BaseEntity() {
    @ManyToOne
    @JoinColumn(name = "society_uuid", nullable = false)
    open var societyEntity: SocietyEntity? = null

    @ManyToOne
    @JoinColumn(name = "student_uuid", nullable = false)
    open var studentEntity: StudentEntity? = null
}
