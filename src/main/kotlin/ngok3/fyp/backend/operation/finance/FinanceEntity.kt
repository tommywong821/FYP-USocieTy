package ngok3.fyp.backend.operation.finance

import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "finance")
open class FinanceEntity(
    open var amount: Number,
    open var description: String,
) : BaseEntity()