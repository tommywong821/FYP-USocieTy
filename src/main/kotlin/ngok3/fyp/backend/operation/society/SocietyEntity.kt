package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.finance.FinanceEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "society")
open class SocietyEntity(
    open var name: String? = null,

    ) : BaseEntity() {

    @OneToMany(mappedBy = "societyEntity")
    open var enrolledSocietyRecordEntities: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "society_finance",
        joinColumns = [JoinColumn(name = "society_uuid")],
        inverseJoinColumns = [JoinColumn(name = "finance_uuid")]
    )
    open var financeRecords: MutableSet<FinanceEntity> = mutableSetOf()
}